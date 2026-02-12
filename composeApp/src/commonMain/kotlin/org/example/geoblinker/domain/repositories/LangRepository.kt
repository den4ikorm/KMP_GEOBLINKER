package org.example.geoblinker.domain.repositories


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


interface LangRepository {
    fun initConstants()
    fun getNameForEvent(name: String): String
    fun getNameForFaq(tag: String): String
