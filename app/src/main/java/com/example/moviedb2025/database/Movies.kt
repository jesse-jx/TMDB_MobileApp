package com.example.moviedb2025.database

import com.example.moviedb2025.models.Movie

class Movies {
    fun getMovies(): List<Movie>{
        return listOf(
            Movie(
                950387,
                "A Minecraft Movie",
                "/yFHHfHcUgGAxziP1C3lLt0q2T4s.jpg",
                "/2Nti3gYAX513wvhp8IiLL6ZDyOm.jpg",
                "2025-03-31",
                "Four misfits find themselves struggling with ordinary problems when they are suddenly pulled through a mysterious portal into the Overworld: a bizarre, cubic wonderland that thrives on imagination. To get back home, they'll have to master this world while embarking on a magical quest with an unexpected, expert crafter, Steve.",
                listOf(10751, 35, 12, 14),
                "tt3566834"
            ),
            Movie(
                822119,
                "Captain America: Brave New World",
                "/4YFyYcUPfrbpj6VpgWh7xoUnwLA.jpg",
                "/jhL4eTpccoZSVehhcR8DKLSBHZy.jpg",
                "2025-02-12",
                "After meeting with newly elected U.S. President Thaddeus Ross, Sam finds himself in the middle of an international incident. He must discover the reason behind a nefarious global plot before the true mastermind has the entire world seeing red.",
                listOf(28, 53, 878),
                "tt14513804"
            ),
            Movie(
                324544,
                "In the Lost Lands",
                "/iHf6bXPghWB6gT8kFkL1zo00x6X.jpg",
                "/op3qmNhvwEvyT7UFyPbIfQmKriB.jpg",
                "2025-02-27",
                "A queen sends the powerful and feared sorceress Gray Alys to the ghostly wilderness of the Lost Lands in search of a magical power, where the sorceress and her guide, the drifter Boyce must outwit and outfight man and demon.",
                listOf(14, 12, 28),
                "tt4419684"
            ),
            Movie(
                696506,
                "Mickey 17",
                "/edKpE9B5qN3e559OuMCLZdW1iBZ.jpg",
                "/hNA73rnG4PjSwgojaC2gbO1f8Rt.jpg",
                "2025-02-28",
                "Unlikely hero Mickey Barnes finds himself in the extraordinary circumstance of working for an employer who demands the ultimate commitment to the job… to die, for a living.",
                listOf(878, 35, 12),
                "tt12299608"
            ),
            Movie(
                762509,
                "Mufasa: The Lion King",
                "/lurEK87kukWNaHd0zYnsi3yzJrs.jpg",
                "/1w8kutrRucTd3wlYyu5QlUDMiG1.jpg",
                "2024-12-18",
                "Mufasa, a cub lost and alone, meets a sympathetic lion named Taka, the heir to a royal bloodline. The chance meeting sets in motion an expansive journey of a group of misfits searching for their destiny.",
                listOf(12, 10751, 16),
                "tt13186482"
            )
        )
    }
}