package com.xuaum.cstv.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.xuaum.cstv.R
import com.xuaum.cstv.data.model.response.getmatchesresponse.CSMatch
import com.xuaum.cstv.databinding.MatchCardBinding
import com.xuaum.cstv.util.MyDateFormatter
import okhttp3.internal.notifyAll
import java.util.*
import kotlin.collections.ArrayList

class MatchesAdapter(
    private val glide: RequestManager,
    private val onCardClicked: (leagueSeries: String, team1Id: Int, team2Id: Int, matchTime: String) -> Unit
) : RecyclerView.Adapter<MatchesAdapter.MatchViewHolder>() {

    private val csMatches: ArrayList<CSMatch> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        return MatchViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.match_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        holder.bind(csMatches[position])
    }

    override fun getItemCount(): Int = csMatches.size

    fun clear() {
        val size = itemCount
        csMatches.clear()
        notifyItemRangeRemoved(0, size)
    }

    fun addAll(csMatchesList: ArrayList<CSMatch>) {
        csMatches.addAll(csMatchesList)
        notifyItemRangeInserted(0, itemCount)
    }

    inner class MatchViewHolder(viewItem: View) : RecyclerView.ViewHolder(viewItem) {
        private val binding = MatchCardBinding.bind(viewItem)

        fun bind(match: CSMatch) {
            val time = match.begin_at
            val team1 = match.opponents[0].opponent
            val team2 = match.opponents[1].opponent
            val league = "${match.league.name} ${match.serie.full_name}"

            binding.cardContainer.setOnClickListener {
                onCardClicked(league, team1.id, team2.id, time)
            }



            binding.leagueName.text = league
            glide
                .load(match.league.image_url)
                .fitCenter()
                .placeholder(R.drawable.circle_placeholder)
                .fallback(R.drawable.circle_placeholder)
                .into(binding.leagueLogo)

            if (match.status == "running") {
                binding.matchTime.isSelected = true
                binding.matchTime.text = "AGORA"
            } else {
                binding.matchTime.isSelected = false
                val dateFormatted = MyDateFormatter().stringToAppDateString(match.begin_at)
                binding.matchTime.text = dateFormatted
            }


            binding.team1Name.text = team1.name
            glide
                .load(team1.image_url)
                .fitCenter()
                .placeholder(R.drawable.circle_placeholder)
                .fallback(R.drawable.circle_placeholder)
                .into(binding.team1Logo)

            binding.team2Name.text = team2.name
            glide
                .load(team2.image_url)
                .fitCenter()
                .placeholder(R.drawable.circle_placeholder)
                .fallback(R.drawable.circle_placeholder)
                .into(binding.team2Logo)
        }
    }
}