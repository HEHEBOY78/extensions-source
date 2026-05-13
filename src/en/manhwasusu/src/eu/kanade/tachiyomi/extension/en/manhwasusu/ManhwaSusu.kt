package eu.kanade.tachiyomi.extension.en.manhwasusu

import eu.kanade.tachiyomi.network.GET
import eu.kanade.tachiyomi.source.model.FilterList
import eu.kanade.tachiyomi.source.model.Page
import eu.kanade.tachiyomi.source.model.SChapter
import eu.kanade.tachiyomi.source.model.SManga
import eu.kanade.tachiyomi.source.online.ParsedHttpSource
import okhttp3.Request
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class ManhwaSusu : ParsedHttpSource() {
    override val name = "ManhwaSusu"
    override val baseUrl = "https://manhwasusu.com"
    override val lang = "en"
    override val supportsLatest = true

    override fun popularMangaRequest(page: Int) =
        GET("$baseUrl/popular/", headers)

    override fun popularMangaSelector() =
        "div.grid > div.overflow-hidden"

    override fun popularMangaFromElement(element: Element) = SManga.create().apply {
        val link = element.selectFirst("a[href]")!!
        setUrlWithoutDomain(link.attr("href"))
        title = element.selectFirst("h2 a")?.text() ?: ""
        thumbnail_url = element.selectFirst("img")?.attr("data-src")
            ?.takeIf { it.isNotEmpty() }
            ?: element.selectFirst("img")?.attr("src")
    }

    override fun popularMangaNextPageSelector() = null

    override fun latestUpdatesRequest(page: Int) =
        GET("$baseUrl/type/manga/", headers)

    override fun latestUpdatesSelector() = popularMangaSelector()
    override fun latestUpdatesFromElement(element: Element) = popularMangaFromElement(element)
    override fun latestUpdatesNextPageSelector() = null

    override fun searchMangaRequest(page: Int, query: String, filters: FilterList) =
        GET("$baseUrl/search?search=$query", headers)

    override fun searchMangaSelector() = popularMangaSelector()
    override fun searchMangaFromElement(element: Element) = popularMangaFromElement(element)
    override fun searchMangaNextPageSelector() = null

    override fun mangaDetailsParse(document: Document) = SManga.create().apply {
        title = document.selectFirst("h1")?.text() ?: ""
        thumbnail_url = document.selectFirst("img[data-src]")?.attr("data-src")
        description = document.selectFirst("div.synopsis, p.description, div.summary")?.text()
    }

    override fun chapterListSelector() = "a[href*='/chapter'], div.chapter a"

    override fun chapterFromElement(element: Element) = SChapter.create().apply {
        setUrlWithoutDomain(element.attr("href"))
        name = element.text()
    }

    override fun pageListParse(document: Document): List<Page> {
        return document.select("img[data-src], div.reader img, img.chapter-img")
            .mapIndexed { i, img ->
                Page(i, "", img.attr("data-src").ifEmpty { img.attr("src") })
            }
    }

    override fun imageUrlParse(document: Document) = ""
}
