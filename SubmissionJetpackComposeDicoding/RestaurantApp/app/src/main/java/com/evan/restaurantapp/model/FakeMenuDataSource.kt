package com.evan.restaurantapp.model

import com.evan.restaurantapp.R

object FakeMenuDataSource {
    val dummyMenus = listOf(
        Menu(1, "Ayam Lalapan", "Ayam goreng disajikan dengan sambal khas dan lalapan segar.", 15000.0, R.drawable.menu_ayam_lalapan),
        Menu(2, "Nasi Goreng", "Nasi goreng dengan bumbu tradisional, dilengkapi dengan telur dan kerupuk.", 12000.0, R.drawable.menu_nasi_goreng),
        Menu(3, "Rendang", "Daging sapi dimasak dengan bumbu rempah yang kaya, empuk dan gurih.", 25000.0, R.drawable.menu_rendang),
        Menu(4, "Gado-Gado", "Sayuran segar dengan saus kacang khas Indonesia, dilengkapi lontong.", 10000.0, R.drawable.menu_gado_gado),
        Menu(5, "Sate", "Sate ayam atau kambing dengan bumbu kacang atau kecap.", 20000.0, R.drawable.menu_sate),
        Menu(6, "Bakso", "Bakso daging sapi disajikan dengan mie dan kuah kaldu.", 15000.0, R.drawable.menu_bakso),
        Menu(7, "Coto", "Sop khas Makassar dengan daging sapi dan bumbu tradisional.", 22000.0, R.drawable.menu_coto),
        Menu(8, "Cumi Goreng", "Cumi-cumi goreng tepung renyah dengan saus sambal atau kecap.", 18000.0, R.drawable.menu_cumi_goreng),
        Menu(9, "Mujair Goreng", "Ikan mujair goreng garing disajikan dengan sambal dan nasi.", 20000.0, R.drawable.menu_mujair_goreng),
        Menu(10, "Mie Goreng", "Mie goreng dengan sayuran dan daging pilihan.", 12000.0, R.drawable.menu_mie_goreng),
        Menu(11, "Es Buah", "Minuman segar dengan campuran buah-buahan tropis dan sirup.", 8000.0, R.drawable.menu_es_buah),
        Menu(12, "Es Cendol", "Minuman manis dengan cendol, santan, dan gula merah.", 7000.0, R.drawable.menu_es_cendol)
    )
}