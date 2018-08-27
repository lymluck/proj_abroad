'use strict';

import React, { Component } from 'react';
import { Platform, Text, StyleSheet, PixelRatio, View, ScrollView, Dimensions, Image, NativeModules, TouchableNativeFeedback } from 'react-native';
import px2dp from '../util/px2dp';
import Avatar from '../component/Avatar';
import Button from '../component/Button';
import theme from '../config/theme';
var quId = 0; var ud; var pushd; var tickt; var aget;
export default class MainPage extends Component {
    constructor(props) {
        super(props);
        this.state = {
            loadedData: false,
            dataBlob: null,
            imageSources: require('../image/good.png'),
            addCount: 0,
            isCollection: true,
        }
    }
    render() {
        return (
            <View style={styles.container}>
                <View style={styles.actionBar}>
                    <TouchableNativeFeedback onPress={this._onPressCallback.bind(this, 4)}>
                        <Image source={require('../image/ic_go_back.png')} style={{ width: px2dp(5), height: px2dp(20), paddingLeft: px2dp(8) }}></Image>
                    </TouchableNativeFeedback>
                    <View style={{ flex: 1, flexDirection: 'column', alignItems: 'center' }}>
                        <Text style={{ color: theme.actionBar.fontColor, fontSize: theme.actionBar.fontSize }}>问答详情</Text>
                    </View>
                </View>
                <ScrollView style={{ flex: 1 }}>
                    <View style={styles.intro}>
                        <Avatar image={{ uri: this.state.dataBlob === null ? "../image/logo_og.png" : this.state.dataBlob.askAvatar }} size={px2dp(32)} textSize={px2dp(15)} defaultSource={require('../image/logo_og.png')} />
                        <View style={{ marginLeft: px2dp(12) }}>
                            <Text style={{ color: theme.text.color, fontSize: px2dp(14) }}>{this.state.dataBlob === null ? "" : this.state.dataBlob.askName}</Text>
                        </View>
                    </View>

                    <View style={styles.introButton}>
                        <View style={styles.button}>
                            <Button text="问" />
                        </View>

                        <View style={styles.question}>
                            <Text style={{ color: theme.text.color, fontSize: px2dp(16) }}>{this.state.dataBlob === null ? "" : this.state.dataBlob.question}</Text>
                        </View>
                    </View>
                    <View style={styles.introText}>
                        <Text style={{ color: '#999999', fontSize: px2dp(11) }}>{this.state.dataBlob === null ? "" : this.state.dataBlob.askTime}</Text>
                    </View>

                    <Text style={{ color: '#999999', fontSize: px2dp(14), paddingLeft: px2dp(16), paddingBottom: px2dp(14), paddingTop: px2dp(14) }}>回复</Text>

                    <View style={styles.introAnswer}>
                        <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                            <Avatar image={{ uri: this.state.dataBlob === null ? "../image/logo_og.png" : this.state.dataBlob.answerAvatarUrl }} size={px2dp(32)} textSize={px2dp(15)} defaultSource={require('../image/logo_og.png')} />

                            <Text style={{ color: theme.text.color, fontSize: px2dp(14), paddingLeft: px2dp(8) }}>{this.state.dataBlob === null ? "" : this.state.dataBlob.answerName}</Text>
                            <TouchableNativeFeedback onPress={this._onPressCallback.bind(this, 0)}>
                                <View style={{ flexDirection: 'row', flex: 1, justifyContent: 'flex-end' }}>

                                    <Image source={this.state.imageSources} style={{ width: px2dp(16), height: px2dp(16) }}></Image>

                                    <Text style={{ color: '#078CF1', paddingLeft: px2dp(4), fontSize: px2dp(11) }}>{this.state.addCount}</Text>
                                </View>
                            </TouchableNativeFeedback>
                        </View>

                        <Text style={{ color: theme.text.color, fontSize: px2dp(16), paddingBottom: px2dp(24), paddingTop: px2dp(10), paddingLeft: px2dp(40) }}>{this.state.dataBlob === null ? "" : this.state.dataBlob.answer}</Text>
                    </View>
                </ScrollView>
                <View style={{
                    backgroundColor: '#fff', width: Dimensions.get('window').width, height: px2dp(50), position: 'absolute', bottom: px2dp(0), borderTopWidth: 1 / PixelRatio.get(),
                    borderTopColor: '#e4e4e4', flexDirection: 'row', paddingLeft: px2dp(30), paddingRight: px2dp(30), justifyContent: 'space-between', alignItems: 'center'
                }}>

                    <TouchableNativeFeedback onPress={this._onPressCallback.bind(this, 1)}>
                        <View style={{ flexDirection: 'column' }}>
                            <Image source={require('../image/share.png')} style={{ width: px2dp(25), height: px2dp(25) }}></Image>
                            <Text>分享</Text>
                        </View>
                    </TouchableNativeFeedback>

                    <TouchableNativeFeedback onPress={this._onPressCallback.bind(this, 2)}>
                        <View style={{ flexDirection: 'column', alignItems: 'center' }}>
                            <Image source={this.state.isCollection ? require('../image/collection_press.png') : require('../image/collection.png')} style={{ width: px2dp(25), height: px2dp(25) }}></Image>
                            <Text>{this.state.isCollection ? "已收藏" : "收藏"}</Text>
                        </View>
                    </TouchableNativeFeedback>
                    <TouchableNativeFeedback onPress={this._onPressCallback.bind(this, 3)}>
                        <View style={{ flexDirection: 'column', alignItems: 'center' }}>
                            <Image source={require('../image/consult.png')} style={{ width: px2dp(25), height: px2dp(25) }}></Image>
                            <Text>在线咨询</Text>
                        </View>
                    </TouchableNativeFeedback>
                </View>
            </View>
        );
    }
    _onPressCallback(position) {
        switch (position) {
            case 0:
                this._like(quId);
                break;

            case 1:
                if (this.state.dataBlob !== null) {
                    var title = this.state.dataBlob.question;
                    var coverUrl = this.state.dataBlob.answerAvatarUrl;
                    this._share(title, title, coverUrl);
                }
                break;

            case 2:
                this._collection();
                break;

            case 3:
                this._jumpMQ();
                break;

            case 4:
                this._finish();
                break;

        }

    }

    _checkCollect() {
        NativeModules.ReactQuModule.checkCollection((flag) => {
            this.setState({
                isCollection: flag,
            }
            );
        });
    }

    _fetchData(id) {
        fetch(`http://api.beikaodi.com/question/${id}`,{
            method:"GET",
        })
            .then((response) => response.json())
            .then((responseData) => {
                let data = responseData.data;
                let dataBlob = {
                    id: id,
                    question: data.question,
                    askTime: data.askTime,
                    answer: data.answer,
                    askName: data.asker.name,
                    askAvatar: data.asker.avatar,
                    answerAvatarUrl: data.answerer.avatarUrl,
                    answerName: data.answerer.name,
                    likedCount: data.likedCount
                }

                this.setState({
                    dataBlob: dataBlob,
                    imageSources: dataBlob.likedCount === 0 ? require('../image/good.png') : require('../image/goodpress.png'),
                    loadedData: true,
                    addCount: data.likedCount,
                });
            }).done();

    }


    componentDidMount() {

        NativeModules.ReactQuModule.getDataFromIntent(
            (id, uid, pushId, ticket, agent) => {
                this._fetchData(id);
                quId = id;
                ud = uid;
                pushd = pushId;
                tickt = ticket;
                aget = agent
            },
            (erroMsg) => { alert(erroMsg) }

        );

        this._checkCollect();
    }


    _share(title, des, coverUrl) {
        NativeModules.ReactQuModule.share(title, des, coverUrl);
    }

    _finish() {
        NativeModules.ReactQuModule.finishActivity();
    }

    _collection() {
        if (this.state.isCollection) {
            NativeModules.ReactQuModule.deleteCollection(result => {
                if (result !== null) {
                    this.setState({
                        isCollection: result,
                    })
                }
            });
            NativeModules.ReactQuModule.showToast("取消成功");
        } else {
            NativeModules.ReactQuModule.addCollection(result => {
                if (result !== null) {
                    this.setState({
                        isCollection: result,
                    })
                }
            });
            NativeModules.ReactQuModule.showToast("收藏成功");

        }

    }

    _jumpMQ() {
        NativeModules.ReactQuModule.jumpMQConversationActivity();
    }

    _like(quId) {
        NativeModules.ReactQuModule.showMessage((flag) => {
            if (flag === true) {
                this.setState({
                    addCount: this.state.addCount + 1,
                });
            }
        }, );
    }

}


const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: theme.pageBackgroundColor
    },
    actionBar: {
        height: theme.actionBar.height,
        backgroundColor: theme.actionBar.backgroundColor,
        width: Dimensions.get('window').width,
        flexDirection: 'row',
        paddingLeft: px2dp(8),
        paddingRight: px2dp(8),
        alignItems: 'center',
        paddingTop: (Platform.OS === 'ios') ? px2dp(20) : 0,
    },
    intro: {
        flexDirection: 'row',
        backgroundColor: '#fff',
        paddingTop: px2dp(18),
        flexWrap: 'wrap',
        alignItems: 'center',
        paddingLeft: px2dp(16),
        borderTopWidth: 1 / PixelRatio.get(),
        borderTopColor: '#e4e4e4',
    },
    introButton: {
        flexDirection: 'row',
        backgroundColor: '#fff',
        paddingTop: px2dp(16),
        flexWrap: 'wrap',
        alignItems: 'flex-start',
        paddingLeft: px2dp(32),
    },
    button: {
        width: px2dp(17),
        backgroundColor: '#F5A623',
        borderRadius: 1,
        marginTop: px2dp(4),
        height: px2dp(17)
    },

    question: {
        flex: 1,
        paddingLeft: px2dp(8),
        paddingRight: px2dp(16)
    },

    introText: {
        flexDirection: 'row',
        backgroundColor: '#fff',
        paddingTop: px2dp(14),
        flexWrap: 'wrap',
        alignItems: 'flex-start',
        paddingLeft: px2dp(57),
        paddingBottom: px2dp(16),
    },

    introAnswer: {
        backgroundColor: '#fff',
        paddingTop: px2dp(18),
        paddingLeft: px2dp(16),
        paddingRight: px2dp(16)
    }

});






