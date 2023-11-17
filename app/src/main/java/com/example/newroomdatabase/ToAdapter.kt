package com.example.newroomdatabase


import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

var listId = ArrayList<Int>()
var backgroundClr:Int = Color.WHITE
class ToAdapter(var datas : List<Datas>,val noteClickListener: NoteClickListener):RecyclerView.Adapter<ToAdapter.viewHolder>()
{

    inner class viewHolder(itemview : View,noteClickListener: NoteClickListener) : RecyclerView.ViewHolder(itemview)
    {

        var title = itemview.findViewById<TextView>(R.id.notetitle)
        var content = itemview.findViewById<TextView>(R.id.notebody)
        var date = itemview.findViewById<TextView>(R.id.date)
        var constraint = itemview.findViewById<ConstraintLayout>(R.id.notelayout)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notelayout,parent,false)
        return viewHolder(view,noteClickListener)
    }


    override fun onBindViewHolder(holder: viewHolder, position: Int) {
       holder.constraint.setBackgroundColor(backgroundClr)

        holder.apply {
            title.text=datas[position].title_content
            content.text=datas[position].body_content
            date.text=datas[position].date
        }
        ///////////

            holder.constraint.setOnClickListener {
                if (listId.size==0)
                {
                    noteClickListener.itemClick(holder.adapterPosition)
                }
                else
                {
                    if(listId.contains(holder.adapterPosition)){
                        listId.remove(holder.adapterPosition)
                        noteClickListener.itemClicktodelete(listId)
                        holder.constraint.setBackgroundColor(ContextCompat.getColor(it.context,R.color.white))

                    }
                    else{

                        listId.add(holder.adapterPosition)
                        holder.constraint.setBackgroundColor(ContextCompat.getColor(it.context,R.color.ligthViolet))
                        noteClickListener.itemClicktodelete(listId)
                    }
                }
            }

          holder.constraint.setOnLongClickListener {
            if (listId.isEmpty()){
            holder.constraint.setBackgroundColor(ContextCompat.getColor(it.context, R.color.ligthViolet))
                //to set background color
            listId.add(holder.adapterPosition)
            noteClickListener.itemClicktodelete(listId)

        }
            true
        }
        //////////////
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    interface NoteClickListener
    {
        fun itemClick(position: Int)
        fun itemClicktodelete(listId:ArrayList<Int>)
    }



    fun clearSelected()   // To clear the selected list
    {
        listId.clear()
        noteClickListener.itemClicktodelete(listId)
    }

    fun update(list1: List<Datas>)  // temperary list update to recycler view while searching
    {
         var datas1 = ArrayList<Datas>()
        datas1.addAll(list1)
        datas = datas1 as List<Datas>
        notifyDataSetChanged()
    }

    fun setAllDataForSelectAll()
    {
        listId.clear()

        for(i in 0 until  datas.size)
            listId.add(i)
        noteClickListener.itemClicktodelete(listId)
    }
    fun setBackgroundColor(tmpColor:Int)
    {
          backgroundClr=tmpColor
        notifyDataSetChanged()
    }

}