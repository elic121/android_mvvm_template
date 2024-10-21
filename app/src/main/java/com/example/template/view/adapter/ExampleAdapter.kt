package com.example.template.view.adapter

import com.example.template.databinding.ItemExampleBinding
import com.example.template.interfaces.ItemClickListener
import com.example.template.model.entity.ExampleEntity
import com.example.template.view.base.BaseAdapter

class ExampleAdapter(
    examples: List<ExampleEntity>,
    itemClickListener: ItemClickListener
) : BaseAdapter<ItemExampleBinding, ExampleEntity>(
    bindingFactory = { layoutInflater, parent, attachToParent ->
        ItemExampleBinding.inflate(layoutInflater, parent, attachToParent)
    },
    list = examples,
    itemClickListener = itemClickListener,
){
    override fun onBindViewHolder(holder: BaseViewHolder<ItemExampleBinding>, position: Int) {
        super.onBindViewHolder(holder, position)

        val example = list[position]
        holder.binding.apply {
            item1.text = example.host
            item2.text = example.userAgent
        }
    }
}