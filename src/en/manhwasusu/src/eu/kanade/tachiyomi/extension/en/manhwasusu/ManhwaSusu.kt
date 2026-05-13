package eu.kanade.tachiyomi.extension.en.manhwasusu

import eu.kanade.tachiyomi.multisrc.madara.Madara

class ManhwaSusu : Madara(
    "ManhwaSusu",
    "https://manhwasusu.com",
    "en",
) {
    override val useNewChapterEndpoint = true
    override val mangaSubString = "manhwa"
}
