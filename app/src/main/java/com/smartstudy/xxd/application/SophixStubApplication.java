package com.smartstudy.xxd.application;

import android.content.Context;
import android.support.annotation.Keep;
import android.support.multidex.MultiDex;

import com.taobao.sophix.SophixApplication;
import com.taobao.sophix.SophixEntry;
import com.taobao.sophix.SophixManager;

/**
 * @author louis
 * @date on 2018/6/26
 * @describe Sophix稳健接入
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class SophixStubApplication extends SophixApplication {

    // 此处SophixEntry应指定真正的Application，并且保证RealApplicationStub类名不被混淆。
    @Keep
    @SophixEntry(XxdApplication.class)
    static class RealApplicationStub {
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        initSophix();
    }

    private void initSophix() {
        String appVersion = "2.15.0";
        try {
            appVersion = this.getPackageManager()
                .getPackageInfo(this.getPackageName(), 0)
                .versionName;
        } catch (Exception e) {
        }
        SophixManager.getInstance().setContext(this)
            .setAppVersion(appVersion)
            .setSecretMetaData("24834080-1", "13d8310939efa9c8f817e23e6f8c8b3a",
                "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCkvq1e84xMYviSPn7lv0NMsH5xd"
                    + "K9MIN5o0m07ItwjmwnKgHxpcVVJzKuj7TOzQfOw+op/jO3L7+vVF9EX3WL+6G/Qm6k3nAOqQTU0B"
                    + "YBNo4vU7lwSladBx798HfTQVBHsfrvey/PS72D1DLc6wmBNZtTw1OItvGehpNJmQ1SI2Q0lMahat"
                    + "TB5bOhaeBLLEhoUINKhWv6xiZsFSeEtZyuX6etajcfJKFwuqmtmP7xTSc39mur9eb+3HR+45cTPh"
                    + "e8pdUwu8BfBEqsqoxoLTY6BMbawBIj1MfNp1MSgua3l5E/BogeHatTZkBrog5oaugvWJbm0fhWIe"
                    + "hd2nPoj9k3DAgMBAAECggEAYLZTPr+I6Drdo7SueB8c2gNuZxpR4kYVasHFQAcJ5pkYWVmuL5RJt"
                    + "kZBqL0SMbSQ459g+Nn5q1QQRQtzQyhC+lPneDY5U5zyBdu76Osl0+4jo3++a2aRRquFfM51Lmt8Dj"
                    + "KkBXoDjBvc9l5dnlAHisjITw0aLjFBQQJqRYBXOvIHabJ7LrSEGR1AK9kNLayAs5MbSod2BE0iLPv"
                    + "dl3tXcE4s42bCLvx3JnMxgne89GE1uVD2Z+4AIlta5KXeGxO/zGapCUxds8Yj1O/tZbKOhAYsAo/0"
                    + "HjRLHzU8GyihVKoLsJJHi9C8Fq81YGn9iF0y5WME8pQQW/+4FZfRNpZM+QKBgQDT5dhAbTZkvI9YO"
                    + "UKHYvqYO9PQv8OpkuR6y6VilSUSjNtqNkrCHC+4O7wwx4eSfm01QUV2ilxpvVT7pyCgjhP3bqnXw5"
                    + "oxhFev4oeGwoJngqRKhpeIwtIb0PZRe0X4Q7p29iCHEOQiwVuYHkD3gbGaR1tTpsUBdcN3Hp+iifI"
                    + "OFQKBgQDHCHe1i6uBaH8zU1NjPPZtdpFg1iJzeGMCJfJGsSok1iIXtHTqsEzz24vL329JKhrv+spe"
                    + "KNApgSlC4leC/T/G+zj4ZuhvItPNvbZg+c+JMMJSW/u2K3b7kL+o2SbyDMqlPf6+MRHU8gyL5TF89"
                    + "CKJ9ffnOxA4RvXP9TEBWX06dwKBgQDS6+AVSWCyZ2s8Q0rw3tpYvHy8NpX57ojVnm5SsekEDQe/Mq"
                    + "m5jDsH5RYPbB2L+aKVNxKAy3rvmhHEqXh1uNcxHYuchUyv628pWZDT2Dvb8xO8RzsBXAOuwpLdSpq"
                    + "8ZBZwodCBAZRE8DlPMxYyCHwT+OF0Y/WH3YQix23ZafxyfQKBgCiWxHrkedxRKCeaZZYGV6bpGCqs"
                    + "YKg1s9M7U9YwyQxsEtXMuk8mFvtgGXlBHpvIF2g7N8vN/O2J5pU6PWh8JuZ4UxztN5d6fcEmYCwUp"
                    + "csvg2FyWrnC0WfqE/vmFPglvanHI807ioxntEFPQzFdRDHoVF72f49KgVSghxbkXhJvAoGBALAEYV"
                    + "dag8jhXUqMfA44x4cHc0oGr1igBt+oG+pphY/p0mkzYRCV3ummgmW+5OYmSjV0880+5iB0i/lu6zV"
                    + "jzB/Xltp5TeCn+b1DsviGpcnJoCWzERGmFf/nF3NzpxOgUv7n926JS5Va1SAvM5nU++rcvTA7RNr8"
                    + "oHzoYYrIJ5fW")
            // zhike 16位aes
            .setAesKey("8842977c7f398b67")
            .setEnableDebug(false)
            .setEnableFullLog()
            .initialize();
    }
}
