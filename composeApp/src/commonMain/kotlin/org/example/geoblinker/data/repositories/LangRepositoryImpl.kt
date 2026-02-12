package org.example.geoblinker.data.repositories


import com.russhwolf.settings.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.example.geoblinker.domain.models.InfoContent
import org.example.geoblinker.domain.repositories.ApiRepository
import org.example.geoblinker.domain.repositories.LangRepository


class LangRepositoryImpl(
    private val api: ApiRepository,
    private val settings: Settings,
    private val json: Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }
) : LangRepository {


    private val langId = 1
    var offerTitle = ""
    var policyTitle = ""
    var offerText = ""
    var policyText = ""
    var aboutTitle = ""
    var aboutText = ""


    private var eventNameMap: MutableMap<String, String> = mutableMapOf()
    private var tracker2EventList: List<String> = emptyList()
    private var tracker4EventList: List<String> = emptyList()


    private val faqNamesTagList: List<String> = listOf(
        "e_faq_1", "e_faq_2", "e_faq_3", "e_faq_4", "e_faq_5"
    )
    private val faqTextMap: MutableMap<String, String> = mutableMapOf()


    init {
        loadFromCache()
    }


    private fun loadFromCache() {
        try {
            val mapJson = settings.getString("trackerNamesMap", "{}")
            eventNameMap = try {
                json.decodeFromString(mapJson)
            } catch (e: Exception) {
                mutableMapOf()
            }


            val t2Json = settings.getString("tracker2EventList", "[]")
            val t4Json = settings.getString("tracker4EventList", "[]")


            tracker2EventList = try { json.decodeFromString(t2Json) } catch (e: Exception) { emptyList() }
            tracker4EventList = try { json.decodeFromString(t4Json) } catch (e: Exception) { emptyList() }


        } catch (e: Exception) {
            println("LangRepository: Error loading cache: $e")
        }
    }


    override fun initConstants() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val trackerResponse = api.getDeviceSignalsData()


                val constants = trackerResponse.data.data.constants


                tracker2EventList = constants.tracker2.getList().list ?: emptyList()
                tracker4EventList = constants.tracker4.getList().list ?: emptyList()


                if (tracker2EventList.isEmpty() || tracker4EventList.isEmpty()) return@launch


                val langResponse = api.getLangData(langId.toString())


                val langValues = langResponse.data.data.langValues


                fillMap(tracker4EventList, langValues)
                fillMap(tracker2EventList, langValues)


                saveToCache()


                faqNamesTagList.forEach { tag ->
                    val translation = langValues[tag]?.get(langId)
                    translation?.let { faqTextMap[tag] = it }
                }


                parseInfoContent("e_about_1_oferta", langValues) { t, b -> offerTitle = t; offerText = b }
                parseInfoContent("e_about_2_pkd", langValues) { t, b -> policyTitle = t; policyText = b }
                parseInfoContent("e_about_app", langValues) { t, b -> aboutTitle = t; aboutText = b }


            } catch (e: Exception) {
                println("LangRepository: Error initConstants: $e")
            }
        }
    }


    private fun fillMap(list: List<String>, langValues: Map<String, Map<Int, String>>) {
        list.forEach { eventCode ->
            val translation = langValues[eventCode]?.get(langId)
            translation?.let { eventNameMap[eventCode] = it }
        }
    }


    private fun parseInfoContent(
        key: String,
        langValues: Map<String, Map<Int, String>>,
        onResult: (title: String, body: String) -> Unit
    ) {
        val jsonString = langValues[key]?.get(langId) ?: return


        if (jsonString.isEmpty() || jsonString == "[]") return


        try {
            val content = json.decodeFromString<InfoContent>(jsonString)
            onResult(content.title, content.body)
        } catch (e: Exception) {
            println("LangRepository: Failed to parse info content for $key: $e")
        }
    }




    private fun saveToCache() {
        try {
            settings.putString("trackerNamesMap", json.encodeToString(eventNameMap))
            settings.putString("tracker2EventList", json.encodeToString(tracker2EventList))
            settings.putString("tracker4EventList", json.encodeToString(tracker4EventList))
        } catch (e: Exception) {
            println("LangRepository: Cache save failed: $e")
        }
    }


    override fun getNameForEvent(name: String): String {
        return eventNameMap[name] ?: ""
    }


    override fun getNameForFaq(tag: String): String {
        return faqTextMap[tag] ?: ""
    }
