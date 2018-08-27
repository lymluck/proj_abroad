'use strict';

import React, {Component} from 'react';
import {Platform} from 'react-native';
import {Navigator} from 'react-native-deprecated-custom-components';
import MainPage from '../page/MainPage';
import SplashActivity from '../native_modules/SplashActivity';

export default class Navigation extends Component{

    render(){
        return(
        <Navigator
            initialRoute={{component: MainPage}}
            renderScene={(route, navigator) => {
                return <route.component navigator={navigator} {...route.args}/>
                }
            }/>
        );
    }

    componentDidMount(){
        if(Platform.OS === 'android')
        SplashActivity.hide();
    }
}