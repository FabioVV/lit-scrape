package org.main

import org.jsoup.Jsoup
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


fun isValidBrlDate(text: String): Boolean {
    try {
        LocalDate.parse(text, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        return true
    } catch (e: Exception) {
        return false
    }
}

fun getFolhetoLinkFromBrlDate(date: String): String {
    if(!isValidBrlDate(date)){
        return ""
    }

    // println(date)

    val formatterMonthYear = DateTimeFormatter.ofPattern("MMMM/yyyy", Locale("pt", "BR"))
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val localDate = LocalDate.parse(date, dateFormatter)

    val curDate = localDate.format(formatterMonthYear).capitalize() // Formato como Mes/ano -> EX: Junho/2020

    val url = "https://arquisp.org.br/folheto-povo-de-deus/"
    val docHTML = Jsoup.connect(url).get()

    val spanWithDate = docHTML.select("span:contains(${curDate})").first()
    val accorDate = spanWithDate?.parent()?.parent() //Pego o accordion da pagina do mes/ano que restamos
    val trs = accorDate?.select("tr")?.toList() ?: emptyList()

    trs?.forEachIndexed { trIndex, tr ->
        val tds = tr.select("td")?.toList() ?: emptyList()

        tds?.forEachIndexed { tdIndex, td ->
            if(td.text() == date){
                val folhetoTd = tds.drop(tdIndex + 1).find { 
                    it.selectFirst("a")?.text() == "Folheto" 
                }

                val href = folhetoTd?.selectFirst("a")?.attr("href")
                if (href != null) {
                    return href
                }

            }
        }

    }

    return ""
}

fun getAllFolhetosLinksFromFmtBrlDate(date: String): Map<String, String> {
    if(!isValidBrlDate(date)){
        return emptyMap()
    }

    // println(date)

    val formatterMonthYear = DateTimeFormatter.ofPattern("MMMM/yyyy", Locale("pt", "BR"))
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val localDate = LocalDate.parse(date, dateFormatter)

    val curDate = localDate.format(formatterMonthYear).capitalize() // Formato como Mes/ano -> EX: Junho/2020

    val url = "https://arquisp.org.br/folheto-povo-de-deus/"
    val docHTML = Jsoup.connect(url).get()

    val spanWithDate = docHTML.select("span:contains(${curDate})").first()
    val accorDate = spanWithDate?.parent()?.parent() //Pego o accordion da pagina do mes/ano que restamos
    val trs = accorDate?.select("tr")?.toList() ?: emptyList()

    val folhetoMap = mutableMapOf<String, String>()

    trs.forEach { tr ->
        val tds = tr.select("td").toList()
        
        tds.forEachIndexed { tdIndex, td ->
            val tdText = td.text()

            if (isValidBrlDate(tdText)) {
                // Look for Folheto in subsequent tds
                val folhetoTd = tds.drop(tdIndex + 1).find { 
                    it.selectFirst("a")?.text() == "Folheto" 
                }

                val href = folhetoTd?.selectFirst("a")?.attr("href")
                if (!href.isNullOrEmpty()) {
                    folhetoMap[tdText] = href
                }
            }
        }
    }

    return folhetoMap
}

fun main() {
    
    println("Dia especifico: 28/06/2026: ${getFolhetoLinkFromBrlDate("28/06/2026")}")
    println()

    println("Do mes: ")
    getAllFolhetosLinksFromFmtBrlDate("28/06/2026").forEach { (date, link) ->
        println("${date}: ${link}")
    }
}

