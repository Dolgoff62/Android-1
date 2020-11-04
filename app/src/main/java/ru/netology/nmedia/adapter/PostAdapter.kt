package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.Utils

interface OnItemClickListener {
    fun onLike(post: Post) {}
    fun onShare(post: Post) {}
    fun onEdit(post: Post) {}
    fun onDelete(post: Post) {}
    fun onCancelEdit(post: Post) {}

}

class PostAdapter(
    private val onItemClickListener: OnItemClickListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onItemClickListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder (
    private val binding: CardPostBinding,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.apply {
            logo.setImageResource(post.avatar)
            tvAuthorPost.text = post.author
            published.text = post.published
            content.text = post.content
            numberOfLikes.text = Utils.formatLikes(post.numberOfLikes)
            numberOfShare.text = Utils.formatLikes(post.numberOfShare)
            numberOfViews.text = Utils.formatLikes(post.numberOfViews)
            likeButton.setImageResource(
                if (post.likeByMe) {
                    R.drawable.ic_baseline_favorite_24
                } else {
                    R.drawable.ic_baseline_favorite_border_24
                }
            )

            ibMenu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.menu_main)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.menuItemDelete -> {
                                onItemClickListener.onDelete(post)
                                true
                            }
                            R.id.menuItemEdit -> {
                                onItemClickListener.onEdit(post)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }

            likeButton.setOnClickListener {
                onItemClickListener.onLike(post)
            }

            toShareButton.setOnClickListener {
                onItemClickListener.onShare(post)
            }
        }
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}

