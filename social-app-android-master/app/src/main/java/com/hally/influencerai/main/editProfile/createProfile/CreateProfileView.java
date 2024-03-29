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

package com.hally.influencerai.main.editProfile.createProfile;

import com.hally.influencerai.main.editProfile.EditProfileView;
import com.hally.influencerai.model.User;

/**
 * Created by HallyTran on 3/22/2019.
 * transon97uet@gmail.com
 */
public interface CreateProfileView extends EditProfileView {

    void buildProfile(User profile);

    void setLocation(String location);
}
