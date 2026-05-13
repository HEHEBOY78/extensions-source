package eu.kanade.tachiyomi.extension.en.manhwasusu

import eu.kanade.tachiyomi.multisrc.madara.Madara
import eu.kanade.tachiyomi.network.GET
import okhttp3.Request

class ManhwaSusu : Madara(
    "ManhwaSusu",
    "https://manhwasusu.com",
    "en",
) {
    override val useNewChapterEndpoint = true

    override fun popularMangaRequest(page: Int): Request {
        val pageStr = if (page > 1) "page/$page/" else ""
        return GET("$baseUrl/type/manga/$pageStr", headers)
    }

    override fun latestUpdatesRequest(page: Int): Request {
        val pageStr = if (page > 1) "page/$page/" else ""
        return GET("$baseUrl/type/manga/${pageStr}?m_orderby=latest", headers)
    }
}
