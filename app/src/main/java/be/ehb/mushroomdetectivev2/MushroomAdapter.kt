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
        //private val photoUriView: TextView = itemView.findViewById(R.id.photo_uri)
        private val apiPoisonView: TextView = itemView.findViewById(R.id.api_poison)
        private val apiConfidenceView: TextView = itemView.findViewById(R.id.api_confidence)

        fun bind(mushroom: Mushroom) {

            // Determine if the mushroom is poisonous or edible, and fill the variable with the proper string resource
            var poisonOrEdible = ""
            poisonOrEdible = if(mushroom.apiPoison == "P"){
                itemView.context.getString(R.string.mushroom_poisonous)
            } else if (mushroom.apiPoison == "E"){
                itemView.context.getString(R.string.mushroom_edible)
            } else {
                itemView.context.getString(R.string.mushroom_unknown)
            }

            capDiameterView.text = mushroom.capDiameter
            capShapeView.text = mushroom.capShape
            capColorView.text = mushroom.capColor
            stemWidthView.text = mushroom.stemWidth
            //photoUriView.text = mushroom.photoUri
            apiPoisonView.text = poisonOrEdible
            apiConfidenceView.text = mushroom.apiConfidence

            // Decode base64 string to bitmap and set it to the ImageView (if there is a photo)
            val photoBase64 = mushroom.photoUri
            if(photoBase64 != null){
                val photoByteArray = android.util.Base64.decode(photoBase64, android.util.Base64.DEFAULT)
                val photoBitmap = android.graphics.BitmapFactory.decodeByteArray(photoByteArray, 0, photoByteArray.size)
                itemView.findViewById<android.widget.ImageView>(R.id.mushroom_photo).setImageBitmap(photoBitmap)
            } else {
                // hide the ImageView
                itemView.findViewById<android.widget.ImageView>(R.id.mushroom_photo).visibility = android.view.View.GONE
            }

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