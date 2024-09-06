package com.learning.mygenai

import kotlinx.coroutines.CoroutineDispatcher

class ApplicationDispatchers(val ioDispatcher: CoroutineDispatcher,val defaultDispatcher:CoroutineDispatcher,val mainDispatcher:CoroutineDispatcher) {


}