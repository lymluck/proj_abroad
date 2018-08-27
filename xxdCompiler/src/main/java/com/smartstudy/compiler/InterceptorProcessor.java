package com.smartstudy.compiler;


import com.smartstudy.annotation.Interceptor;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import static com.smartstudy.compiler.Consts.CLASS_JAVA_DOC;
import static com.smartstudy.compiler.Consts.HANDLE;
import static com.smartstudy.compiler.Consts.INTERCEPTOR_ANNOTATION_TYPE;
import static com.smartstudy.compiler.Consts.INTERCEPTOR_INTERFACE;
import static com.smartstudy.compiler.Consts.INTERCEPTOR_TABLE;
import static com.smartstudy.compiler.Consts.OPTION_MODULE_NAME;
import static com.smartstudy.compiler.Consts.PACKAGE_NAME;


/**
 * {@link Interceptor} annotation processor.
 * <p>
 * Created by louis on 2017/3/6.
 */
@SupportedAnnotationTypes(INTERCEPTOR_ANNOTATION_TYPE)
@SupportedOptions(OPTION_MODULE_NAME)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class InterceptorProcessor extends AbstractProcessor {
    private String mModuleName;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mModuleName = processingEnvironment.getOptions().get(OPTION_MODULE_NAME);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Interceptor.class);
        if (elements == null || elements.isEmpty()) {
            return true;
        }
        // 合法的TypeElement集合
        Set<TypeElement> typeElements = new HashSet<>();
        for (Element element : elements) {
            if (validateElement(element)) {
                typeElements.add((TypeElement) element);
            }
        }

        if (mModuleName != null) {
            String validModuleName = mModuleName.replace(".", "_").replace("-", "_");
            generateInterceptors(validModuleName, typeElements);
        }
        return true;
    }

    private boolean validateElement(Element element) {
        return element.getKind().isClass() && processingEnv.getTypeUtils().isAssignable(element.asType(),
                processingEnv.getElementUtils().getTypeElement(INTERCEPTOR_INTERFACE).asType());
    }

    private void generateInterceptors(String moduleName, Set<TypeElement> elements) {
        /*
         * params
         */
        TypeElement interceptorType = processingEnv.getElementUtils().getTypeElement(INTERCEPTOR_INTERFACE);
        // Map<String, Class<? extends RouteInterceptor>> map
        ParameterizedTypeName mapTypeName = ParameterizedTypeName.get(ClassName.get(Map.class),
                ClassName.get(String.class), ParameterizedTypeName.get(ClassName.get(Class.class),
                        WildcardTypeName.subtypeOf(ClassName.get(interceptorType))));
        ParameterSpec mapParameterSpec = ParameterSpec.builder(mapTypeName, "map").build();
        /*
         * method
         */
        MethodSpec.Builder handleInterceptors = MethodSpec.methodBuilder(HANDLE)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(mapParameterSpec);
        for (TypeElement element : elements) {
            Interceptor interceptor = element.getAnnotation(Interceptor.class);
            String name = interceptor.value();
            handleInterceptors.addStatement("map.put($S, $T.class)", name, ClassName.get(element));
        }

        /*
         * class
         */
        TypeSpec type = TypeSpec.classBuilder(capitalize(moduleName) + INTERCEPTOR_TABLE)
                .addSuperinterface(ClassName.get(PACKAGE_NAME, INTERCEPTOR_TABLE))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(handleInterceptors.build())
                .addJavadoc(CLASS_JAVA_DOC)
                .build();

        try {
            JavaFile.builder(PACKAGE_NAME, type).build().writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String capitalize(CharSequence self) {
        return self.length() == 0 ? "" :
                "" + Character.toUpperCase(self.charAt(0)) + self.subSequence(1, self.length());
    }

}
