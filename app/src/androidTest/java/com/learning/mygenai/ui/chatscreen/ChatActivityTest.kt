package com.learning.mygenai.ui.chatscreen

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.learning.mygenai.R
import okhttp3.internal.wait
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test


class ChatActivityTest{

    @get:Rule
    val activityScenarioRule=ActivityScenarioRule<ChatActivity>(ChatActivity::class.java)


    @Test
    fun perform_prompt_enter() {
        onView(withId(R.id.prompt)).perform(typeText("How are you"))
        onView(withId(R.id.askButton)).perform(click())
//        Thread.sleep(1000)
    }

}