/*
 * Copyright (c) 2017.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.chatsdk.model;

import android.os.Parcelable;

/**
 * Created by Anthony Ngure on 04/10/2017.
 * Email : anthonyngure25@gmail.com.
 */

public interface BaseUser extends Parcelable {

    long getId();

    String getAvatar();

    String getName();


}
