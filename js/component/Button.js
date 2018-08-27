/**
 * Created by wangdi on 4/11/16.
 */
'use strict';

import React, {Component} from 'react';
import {Text, View, StyleSheet, Platform, TouchableHighlight, TouchableNativeFeedback} from 'react-native';
import px2dp from '../util/px2dp';
import theme from '../config/theme';
import {PropTypes} from 'prop-types';

export default class Button extends Component{
    static propTypes = {
        text: PropTypes.string.isRequired,
    };

    render(){
        if(Platform.OS === 'android') {
            return (
                <TouchableNativeFeedback
                    onPress={this.props.onPress}>
                    {this._renderContent()}
                </TouchableNativeFeedback>
            );
        }else if(Platform.OS === 'ios'){
            return(
                <TouchableHighlight
                    style={{flex: 1, height: px2dp(45)}}
                    onPress={this.props.onPress}
                    activeOpacity={theme.btnActiveOpacity}>
                    {this._renderContent()}
                </TouchableHighlight>
            );
        }
    }

    _renderContent(){
        return(
            <View style={{flex: 1, height: px2dp(45),  alignItems:'center', justifyContent:'center',
                borderRadius: 1}}>
                <Text style={styles.text}>{this.props.text}</Text>
            </View>
        );
    }
}

const styles = StyleSheet.create({
   text:{
       color: 'white',
       fontSize: px2dp(13)
   }
});