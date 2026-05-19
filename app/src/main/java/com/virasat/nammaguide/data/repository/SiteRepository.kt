package com.virasat.nammaguide.data.repository

import android.content.Context
import com.virasat.nammaguide.data.db.CheckInEntity
import com.virasat.nammaguide.data.db.VirasatDatabase
import com.virasat.nammaguide.data.model.HeritageSite
import kotlinx.coroutines.flow.Flow

/**
 * Single source of truth for heritage site data and check-in persistence.
 *
 * Site data is hardcoded (simulated GPS — no real location needed).
 * Check-in data is persisted via Room DB.
 */
class SiteRepository(context: Context) {

    private val checkInDao = VirasatDatabase.getInstance(context).checkInDao()

    // ─── Simulated Heritage Site Data ─────────────────────────────────────────

    val allSites: List<HeritageSite> = listOf(

        HeritageSite(
            id = "KA001",
            nameEn = "Vittala Temple, Hampi",
            nameKn = "ವಿಟ್ಟಲ ದೇವಾಲಯ, ಹಂಪಿ",
            latitude = 15.3350,
            longitude = 76.4600,
            taglineEn = "Where stone pillars sing",
            taglineKn = "ಕಲ್ಲಿನ ಕಂಬಗಳು ಹಾಡುವ ಸ್ಥಳ",
            descriptionEn = "The Vittala Temple at Hampi is the crowning glory of Vijayanagara architecture. Built in the 15th century, it stands as a testament to the empire's opulence and its artists' mastery. The stone chariot in the courtyard — emblem of Karnataka — and the famous musical pillars that resonate with different tones when tapped, draw pilgrims and scholars from across the world.",
            descriptionKn = "ಹಂಪಿಯ ವಿಟ್ಟಲ ದೇವಾಲಯವು ವಿಜಯನಗರ ವಾಸ್ತುಶಿಲ್ಪದ ಶ್ರೇಷ್ಠ ಕೃತಿಯಾಗಿದೆ. 15ನೇ ಶತಮಾನದಲ್ಲಿ ನಿರ್ಮಿತವಾದ ಈ ದೇವಾಲಯವು ಸಾಮ್ರಾಜ್ಯದ ವೈಭವ ಮತ್ತು ಕಲಾವಿದರ ಕೌಶಲ್ಯದ ಸಾಕ್ಷಿಯಾಗಿದೆ.",
            architecturalNote = "Dravidian style with elaborate gopurams. The 56 musical pillars (sapta svara stambhas) are carved from monolithic granite and produce distinct musical notes. The stone chariot (ratha) in the courtyard is Karnataka's state symbol.",
            architecturalNoteKn = "ವಿಸ್ತೃತ ಗೋಪುರಗಳೊಂದಿಗೆ ದ್ರಾವಿಡ ಶೈಲಿ. 56 ಸಂಗೀತ ಕಂಬಗಳು (ಸಪ್ತಸ್ವರ ಸ್ತಂಭಗಳು) ಏಕಶಿಲಾ ಗ್ರಾನೈಟ್‌ನಿಂದ ಕೆತ್ತಲಾಗಿದ್ದು ವಿಶಿಷ್ಟ ಸಂಗೀತ ನಾದಗಳನ್ನು ಉತ್ಪಾದಿಸುತ್ತವೆ.",
            localLegend = "Legend holds that Lord Vishnu himself appeared as Vittala (a form of Venkatesha) and demanded a temple here. When King Devaraya II objected to the humble structure, Vittala stamped his foot, leaving an imprint on the stone floor — still visible near the main sanctum.",
            localLegendKn = "ಐತಿಹ್ಯದ ಪ್ರಕಾರ ವಿಷ್ಣು ಸ್ವತಃ ವಿಟ್ಟಲ ರೂಪದಲ್ಲಿ ಪ್ರತ್ಯಕ್ಷನಾಗಿ ಇಲ್ಲಿ ದೇವಾಲಯ ನಿರ್ಮಿಸಬೇಕೆಂದು ಕೋರಿದನು.",
            hiddenFact = "The musical pillars were tested by British engineers in 1876 who suspected hollow metal pipes inside. They cut two pillars open — found only solid granite. The mystery of the music remains unsolved.",
            hiddenFactKn = "1876ರಲ್ಲಿ ಬ್ರಿಟಿಷ್ ಇಂಜಿನಿಯರ್‌ಗಳು ಎರಡು ಕಂಬಗಳನ್ನು ಕತ್ತರಿಸಿ ಪರೀಕ್ಷಿಸಿದರು — ಒಳಗೆ ಕೇವಲ ಘನ ಗ್ರಾನೈಟ್ ಇತ್ತು. ಸಂಗೀತದ ರಹಸ್ಯ ಇಂದಿಗೂ ಉಳಿದಿದೆ.",
            distanceKm = 12.3,
            audioResId = 0 // Replace with R.raw.audio_kaXXX after adding MP3 files to res/raw/
        ),

        HeritageSite(
            id = "KA002",
            nameEn = "Chennakeshava Temple, Belur",
            nameKn = "ಚೆನ್ನಕೇಶವ ದೇವಾಲಯ, ಬೇಲೂರು",
            latitude = 13.1650,
            longitude = 75.8600,
            taglineEn = "Star-shaped stone poetry in 42 doorways",
            taglineKn = "42 ಬಾಗಿಲುಗಳಲ್ಲಿ ಕೆತ್ತಿದ ಕಾವ್ಯ",
            descriptionEn = "Commissioned by King Vishnuvardhana in 1117 CE to celebrate his victory over the Cholas, the Chennakeshava Temple took over 103 years to complete. Its star-shaped platform, intricate bracket figures (Madanikas), and friezes of elephants, lions, horses, scrollwork, and epics make it a living encyclopaedia of Hoysala art.",
            descriptionKn = "1117 CE ಯಲ್ಲಿ ರಾಜ ವಿಷ್ಣುವರ್ಧನನಿಂದ ಚೋಳರ ಮೇಲಿನ ವಿಜಯವನ್ನು ಆಚರಿಸಲು ನಿರ್ಮಿಸಲಾಗಿದ್ದು 103 ವರ್ಷಗಳಲ್ಲಿ ಪೂರ್ಣಗೊಂಡಿತು.",
            architecturalNote = "Hoysala style on a stellate (star-shaped) platform. 42 decorated doorways. The outer walls feature 11 horizontal friezes including elephants at the base, then horses, then floral scrolls, then epic scenes, then yali (mythical lions), then a hamsapaṭṭi (geese) frieze.",
            architecturalNoteKn = "ಹೊಯ್ಸಳ ಶೈಲಿ, ತಾರಾ ಆಕಾರದ ವೇದಿಕೆ. 42 ಅಲಂಕೃತ ಬಾಗಿಲುಗಳು. ಹೊರ ಗೋಡೆಗಳಲ್ಲಿ 11 ಅಡ್ಡ ಫ್ರೀಜ್‌ಗಳಿವೆ.",
            localLegend = "It is said that sculptor Jakanachari, the chief architect, was visited by his long-lost son who pointed out a flaw in a carved elephant's tusk. Jakanachari wept and cut off his own hand, then reattached it through divine grace — the imperfect tusk is still visible today.",
            localLegendKn = "ಶಿಲ್ಪಿ ಜಕನಾಚಾರಿ ತನ್ನ ಮಗನು ಆನೆಯ ದಂತದಲ್ಲಿ ದೋಷ ತೋರಿಸಿದಾಗ ದುಃಖದಿಂದ ತನ್ನ ಕೈ ಕತ್ತರಿಸಿಕೊಂಡನು. ಆ ದೋಷಪೂರಿತ ದಂತ ಇಂದಿಗೂ ಕಾಣಸಿಗುತ್ತದೆ.",
            hiddenFact = "The Darpanasundari (mirror-gazing beauty) figure on the tower is so detailed that the earrings she is wearing reflect actual light — achieved without any mirrors, purely through the angle and polish of the chloritic schist.",
            hiddenFactKn = "ಗೋಪುರದ ಮೇಲಿನ ದರ್ಪಣಸುಂದರಿ ಶಿಲ್ಪ ಎಷ್ಟು ವಿಸ್ತೃತವಾಗಿದೆ ಎಂದರೆ ಅವಳ ಕಿವಿಯೋಲೆಗಳು ನಿಜವಾದ ಬೆಳಕನ್ನು ಪ್ರತಿಫಲಿಸುತ್ತವೆ.",
            distanceKm = 47.8,
            audioResId = 0 // Replace with R.raw.audio_kaXXX after adding MP3 files to res/raw/
        ),

        HeritageSite(
            id = "KA003",
            nameEn = "Badami Cave Temples",
            nameKn = "ಬಾದಾಮಿ ಗುಹಾ ದೇವಾಲಯಗಳು",
            latitude = 15.9167,
            longitude = 75.6833,
            taglineEn = "Chalukya rock-cut wonders of the 6th century",
            taglineKn = "6ನೇ ಶತಮಾನದ ಚಾಲುಕ್ಯ ಶಿಲಾ ಕಲೆ",
            descriptionEn = "Carved into the sandstone cliffs above a sacred lake (Agastya Tirtha), the four cave temples of Badami represent the earliest flowering of Deccan rock-cut architecture. Dedicated to Shiva, Vishnu (two caves), and Jain Tirthankaras, they were commissioned by Pulakesi I around 543 CE.",
            descriptionKn = "ಪವಿತ್ರ ಸರೋವರದ ಮೇಲಿನ ಮರಳುಗಲ್ಲು ಬಂಡೆಗಳಲ್ಲಿ ಕೆತ್ತಲಾದ ನಾಲ್ಕು ಗುಹಾ ದೇವಾಲಯಗಳು ದಕ್ಕನ್ ಶಿಲಾ ವಾಸ್ತುಶಿಲ್ಪದ ಮೊದಲ ಅರಳುವಿಕೆಯನ್ನು ಪ್ರತಿನಿಧಿಸುತ್ತವೆ.",
            architecturalNote = "Early Chalukya style, 543–598 CE. Cave 1 (Shiva) has an 18-arm Nataraja; Cave 2 (Vishnu) has Trivikrama spanning the cosmos in three strides; Cave 3 (the largest) shows Vishnu in Ananthashayana pose on the serpent Shesha; Cave 4 is Jain.",
            architecturalNoteKn = "ಆರಂಭಿಕ ಚಾಲುಕ್ಯ ಶೈಲಿ, 543–598 CE. ಗುಹೆ 1 (ಶಿವ) 18 ತೋಳಿನ ನಟರಾಜ; ಗುಹೆ 2 (ವಿಷ್ಣು) ತ್ರಿವಿಕ್ರಮ; ಗುಹೆ 3 ಅನಂತಶಯನ.",
            localLegend = "The sacred lake Agastya Tirtha is said to have been created when sage Agastya struck the ground with his staff to quench the thirst of his disciples during a drought. The blood-red sandstone cliffs, locals say, bleed when it rains heavily — a sign of the mountain's own devotion.",
            localLegendKn = "ಅಗಸ್ತ್ಯ ತೀರ್ಥ ಸರೋವರವನ್ನು ಋಷಿ ಅಗಸ್ತ್ಯ ತನ್ನ ದಂಡದಿಂದ ಹೊಡೆದು ಸೃಷ್ಟಿಸಿದನೆಂದು ಐತಿಹ್ಯ.",
            hiddenFact = "Cave 3 at Badami contains what may be the world's oldest dated image of Vishnu in Trivikrama pose, along with an inscription dated to 578 CE making it one of the earliest reliably dated examples of Brahmi script in the Deccan.",
            hiddenFactKn = "ಗುಹೆ 3 ರಲ್ಲಿ 578 CE ಯ ಶಾಸನ ಸಹಿತ ವಿಷ್ಣು ತ್ರಿವಿಕ್ರಮ ಪ್ರತಿಮೆ ಇದ್ದು ಇದು ದಕ್ಕನ್‌ನ ಅತ್ಯಂತ ಹಳೆಯ ಬ್ರಾಹ್ಮಿ ಲಿಪಿ ಶಾಸನಗಳಲ್ಲೊಂದಾಗಿದೆ.",
            distanceKm = 89.2,
            audioResId = 0 // Replace with R.raw.audio_kaXXX after adding MP3 files to res/raw/
        ),

        HeritageSite(
            id = "KA004",
            nameEn = "Gommateshwara, Shravanabelagola",
            nameKn = "ಗೊಮ್ಮಟೇಶ್ವರ, ಶ್ರವಣಬೆಳಗೊಳ",
            latitude = 12.8561,
            longitude = 76.4894,
            taglineEn = "The world's tallest monolithic statue",
            taglineKn = "ವಿಶ್ವದ ಅತಿ ಎತ್ತರದ ಏಕಶಿಲಾ ಪ್ರತಿಮೆ",
            descriptionEn = "Standing 17.7 metres tall atop Vindyagiri Hill, the Gommateshwara (Bahubali) statue was commissioned by the Ganga dynasty minister Chavundaraya in 983 CE. Carved from a single granite boulder, it depicts Bahubali in deep meditation, so still that creepers grow up his legs and termite mounds rise around his feet — symbolizing his years of unmoving penance.",
            descriptionKn = "ವಿಂಧ್ಯಗಿರಿ ಬೆಟ್ಟದ ಮೇಲೆ 17.7 ಮೀಟರ್ ಎತ್ತರ ನಿಂತಿರುವ ಗೊಮ್ಮಟೇಶ್ವರ (ಬಾಹುಬಲಿ) ಪ್ರತಿಮೆಯನ್ನು 983 CE ಯಲ್ಲಿ ಗಂಗ ವಂಶದ ಮಂತ್ರಿ ಚಾವುಂಡರಾಯ ಕೆತ್ತಿಸಿದರು.",
            architecturalNote = "Digambara Jain tradition. The statue is carved from a single granite outcrop on the hilltop — unique in world sculpture. The figure's proportions follow the Jain iconometric canon: elongated arms reaching the knees, serene face with downcast eyes. The Mahamastakabhisheka festival every 12 years bathes it in milk, saffron, and flowers.",
            architecturalNoteKn = "ದಿಗಂಬರ ಜೈನ ಸಂಪ್ರದಾಯ. ಪ್ರತಿಮೆ ಬೆಟ್ಟದ ಮೇಲಿರುವ ಏಕ ಗ್ರಾನೈಟ್ ಬಂಡೆಯಿಂದ ಕೆತ್ತಲಾಗಿದೆ.",
            localLegend = "Bahubali, son of the first Tirthankara Rishabhanatha, renounced his kingdom after defeating his brother in a duel. He stood motionless for a year in penance. The creepers on his legs and the anthills at his feet are reminders of his absolute stillness — even ants mistook him for a tree.",
            localLegendKn = "ಬಾಹುಬಲಿ ತನ್ನ ಸಹೋದರನನ್ನು ಸೋಲಿಸಿದ ನಂತರ ರಾಜ್ಯ ತ್ಯಜಿಸಿ ಒಂದು ವರ್ಷ ನಿಶ್ಚಲನಾಗಿ ನಿಂತು ತಪಸ್ಸು ಮಾಡಿದನು.",
            hiddenFact = "The statue is so perfectly balanced that it has withstood multiple earthquakes without a crack. Geologists attribute this to the sculptor's masterful use of the granite's natural grain direction — essentially turning the stone's inherent stress lines into load-bearing pillars.",
            hiddenFactKn = "ಪ್ರತಿಮೆ ಅನೇಕ ಭೂಕಂಪಗಳನ್ನು ಯಾವುದೇ ಬಿರುಕಿಲ್ಲದೆ ತಡೆದಿದೆ. ಶಿಲ್ಪಿ ಗ್ರಾನೈಟ್‌ನ ನೈಸರ್ಗಿಕ ಧಾನ್ಯ ದಿಕ್ಕನ್ನು ಬಳಸಿ ಆಧಾರ ಸ್ತಂಭಗಳನ್ನಾಗಿ ಮಾಡಿದ್ದಾರೆ.",
            distanceKm = 156.4,
            audioResId = 0 // Replace with R.raw.audio_kaXXX after adding MP3 files to res/raw/
        ),

        HeritageSite(
            id = "KA005",
            nameEn = "Durga Temple, Aihole",
            nameKn = "ದುರ್ಗಾ ದೇವಾಲಯ, ಐಹೊಳೆ",
            latitude = 15.9603,
            longitude = 75.8875,
            taglineEn = "The cradle of Indian temple architecture",
            taglineKn = "ಭಾರತೀಯ ದೇವಾಲಯ ವಾಸ್ತುಶಿಲ್ಪದ ತೊಟ್ಟಿಲು",
            descriptionEn = "Aihole, with its 125+ temples built between the 6th and 12th centuries, is called 'the cradle of Indian temple architecture.' The Durga Temple (c. 7th–8th century CE) stands out for its apsidal (semi-circular) plan — a form borrowed from Buddhist chaitya halls — and its magnificent ambulatory gallery of sculpted panels from the Puranas.",
            descriptionKn = "6ನೇ ಮತ್ತು 12ನೇ ಶತಮಾನದ ನಡುವೆ ನಿರ್ಮಿಸಲಾದ 125ಕ್ಕೂ ಹೆಚ್ಚು ದೇವಾಲಯಗಳೊಂದಿಗೆ ಐಹೊಳೆಯನ್ನು 'ಭಾರತೀಯ ದೇವಾಲಯ ವಾಸ್ತುಶಿಲ್ಪದ ತೊಟ್ಟಿಲು' ಎಂದು ಕರೆಯಲಾಗುತ್ತದೆ.",
            architecturalNote = "Early Chalukya style exhibiting an experimental blend of Nagara and Dravidian elements. The apsidal plan (semi-circular apse) is unique among Hindu temples and shows the Chalukya architects learning from and synthesising Buddhist, Jain, and Hindu temple forms simultaneously.",
            architecturalNoteKn = "ಆರಂಭಿಕ ಚಾಲುಕ್ಯ ಶೈಲಿ ನಾಗರ ಮತ್ತು ದ್ರಾವಿಡ ಅಂಶಗಳ ಪ್ರಯೋಗಾತ್ಮಕ ಮಿಶ್ರಣ. ಅರ್ಧವೃತ್ತಾಕಾರ ಯೋಜನೆ ಹಿಂದೂ ದೇವಾಲಯಗಳಲ್ಲಿ ಅನನ್ಯವಾಗಿದೆ.",
            localLegend = "The temple is named 'Durga' not for the goddess but for the word 'durgā' meaning fort — it once stood within or near a fort. Local legend says a princess of Aihole used to meditate here nightly, and her ghostly lamp is still seen by watchmen on moonless nights.",
            localLegendKn = "ದೇವಾಲಯಕ್ಕೆ 'ದುರ್ಗ' ಹೆಸರು ದೇವಿಗಾಗಿ ಅಲ್ಲ, ಕೋಟೆ ಎಂಬ ಅರ್ಥದ 'ದುರ್ಗ' ಪದದಿಂದ ಬಂದಿದೆ.",
            hiddenFact = "The Aihole inscription of 634 CE (the Meguti Jain temple nearby) by the court poet Ravikirti is the first known inscription in classical Sanskrit to mention the name 'Bharata' for India — making Aihole arguably the birthplace of India's literary identity.",
            hiddenFactKn = "634 CE ಯ ಐಹೊಳೆ ಶಾಸನ ಭಾರತ ಎಂಬ ಹೆಸರನ್ನು ಉಲ್ಲೇಖಿಸುವ ಮೊದಲ ತಿಳಿದ ಸಂಸ್ಕೃತ ಶಾಸನವಾಗಿದೆ.",
            distanceKm = 103.7,
            audioResId = 0 // Replace with R.raw.audio_kaXXX after adding MP3 files to res/raw/
        )
    )

    fun getSiteById(id: String): HeritageSite? = allSites.find { it.id == id }

    // ─── Check-In Operations (Room) ──────────────────────────────────────────

    fun getAllCheckIns(): Flow<List<com.virasat.nammaguide.data.db.CheckInEntity>> =
        checkInDao.getAllCheckIns()

    fun isCheckedIn(siteId: String): Flow<Boolean> =
        checkInDao.isCheckedIn(siteId)

    suspend fun checkIn(site: HeritageSite) {
        val entity = CheckInEntity(
            siteId = site.id,
            siteName = site.nameEn,
            timestamp = System.currentTimeMillis(),
            isCheckedIn = true
        )
        checkInDao.insert(entity)
    }
}
