/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyphenate.chatuidemo;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

/**
 * {@link com.hyphenate.easeui.widget.EaseChatExtendMenu}
 * {@link com.hyphenate.easeui.widget.chatrow.EaseChatRow}
 */

public class DemoApplication{

	public static Context applicationContext;
	private static DemoApplication instance;
	// login user name
	public final String PREF_USERNAME = "username";
	
	/**
	 * 当前用户nickname,为了苹果推送,不是userid而是昵称
	 */
	public static String currentUserNick = "";
	public static String currentUserName = "";

	public void init(MultiDexApplication application) {
        applicationContext = application;
        instance = this;
        
        //init demo helper
        DemoHelper.getInstance().init(applicationContext);
	}

	public static DemoApplication getInstance() {
		if (instance == null){
			instance = new DemoApplication();
		}
		return instance;
	}

	public static Context getApplicationContext(){
		return applicationContext;
	}

}
