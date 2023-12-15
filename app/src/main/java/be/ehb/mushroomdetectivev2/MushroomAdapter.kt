package be.ehb.mushroomdetectivev2

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MushroomAdapter : RecyclerView.Adapter<MushroomAdapter.MushroomViewHolder>() {

    private var mushrooms = emptyList<Mushroom>()

    class MushroomViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.mushroom_list_item, parent, false)) {
        private val capDiameterView: TextView = itemView.findViewById(R.id.cap_diameter)
        private val capShapeView: TextView = itemView.findViewById(R.id.cap_shape)
        private val capColorView: TextView = itemView.findViewById(R.id.cap_color)
        private val stemWidthView: TextView = itemView.findViewById(R.id.stem_width)

        private val apiPoisonView: TextView = itemView.findViewById(R.id.api_poison)
        private val apiConfidenceView: TextView = itemView.findViewById(R.id.api_confidence)

        fun bind(mushroom: Mushroom) {
            capDiameterView.text = mushroom.capDiameter
            capShapeView.text = mushroom.capShape
            capColorView.text = mushroom.capColor
            stemWidthView.text = mushroom.stemWidth
            apiPoisonView.text = mushroom.apiPoison
            apiConfidenceView.text = mushroom.apiConfidence

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MushroomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MushroomViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MushroomViewHolder, position: Int) {
        val mushroom = mushrooms[position]
        holder.bind(mushroom)
    }

    override fun getItemCount() = mushrooms.size

    internal fun setMushrooms(mushrooms: List<Mushroom>) {
        this.mushrooms = mushrooms
        notifyDataSetChanged()
    }
}