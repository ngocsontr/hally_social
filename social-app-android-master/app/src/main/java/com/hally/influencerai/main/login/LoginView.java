/*
 * Copyright 2018
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.hally.influencerai.main.login;

import com.hally.influencerai.main.base.BaseView;
import com.hally.influencerai.model.SocialUser;

/**
 * Created by Alexey on 03.05.18.
 */

public interface LoginView extends BaseView {

    void signInWithGoogle();

    void signInWithFacebook();

    void signInWithInstagram();

    void signInWithTwitter();

    void startCreateProfileActivity(SocialUser user);
}
