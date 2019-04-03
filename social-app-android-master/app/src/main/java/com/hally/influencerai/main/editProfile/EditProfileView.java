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

package com.hally.influencerai.main.editProfile;

import com.hally.influencerai.main.pickImageBase.PickImageView;
import com.hally.influencerai.model.User;

/**
 * Created by HallyTran on 03.05.18.
 */

public interface EditProfileView extends PickImageView {

    /**
     * Load the exist user
     *
     * @param user user
     */
    void loadProfile(User user);

    /**
     * view professions list
     */
    void createProfessionView();

    /**
     * @param view sign up view
     * @return true if all field was validated
     */
    boolean doValidate(EditProfileView view);
}
