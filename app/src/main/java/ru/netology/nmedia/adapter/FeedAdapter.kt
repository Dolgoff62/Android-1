package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardAdBinding
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Ad
import ru.netology.nmedia.dto.FeedItem
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.Utils
import ru.netology.nmedia.view.load

interface OnItemClickListener {
    fun onLike(post: Post) {}
    fun onEdit(post: Post) {}
    fun onDelete(post: Post) {}
    fun onPost(post: Post) {}
    fun onPhoto(post: Post) {}
    fun onAdClick(ad: Ad) {}
}

class FeedAdapter(
    private val onItemClickListener: OnItemClickListener
) : PagingDataAdapter<FeedItem, RecyclerView.ViewHolder>(FeedDiffCallback()) {

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is Ad -> R.layout.card_ad
            is Post -> R.layout.card_post
            null -> throw IllegalArgumentException("Unknown item type")
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            R.layout.card_post -> {
                val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return PostViewHolder(binding, onItemClickListener)
            }
            R.layout.card_ad -> {
                val binding = CardAdBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return AdViewHolder(binding, onItemClickListener)
            }
            else -> throw IllegalArgumentException("Unknown item type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(val item = getItem(position)) {
            is Ad -> (holder as? AdViewHolder)?.bind(item)
            is Post -> (holder as? PostViewHolder)?.bind(item)
            null -> throw IllegalArgumentException("Unknown item type")
        }
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.apply {
            tvAuthorPost.text = post.author

            val url = "http://10.0.2.2:9999/avatars/${post.authorAvatar}"
            val urlAttach = "http://10.0.2.2:9999/media/${post.attachment?.url}"

            if (post.authorAvatar != "") {

                Glide.with(logo.context)
                    .load(url)
                    .circleCrop()
                    .timeout(30_000)
                    .into(logo)
            }

            published.text = Utils.convertDate(post.published)
            content.text = post.content
            likeButton.text = Utils.formatLikes(post.numberOfLikes)
            likeButtonChange(post)

            if (post.attachment == null) {
                binding.postImageAttachment.visibility = View.GONE
            } else {
                binding.postImageAttachment.visibility = View.VISIBLE
                Glide.with(binding.postImageAttachment.context)
                    .load(urlAttach)
                    .timeout(30_000)
                    .into(binding.postImageAttachment)
            }

            ibMenu.visibility = if (post.ownedByMe) View.VISIBLE else View.INVISIBLE

            ibMenu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.menu_options)
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

            content.setOnClickListener {
                onItemClickListener.onPost(post)
            }

            postImageAttachment.setOnClickListener {
                onItemClickListener.onPhoto(post)
            }
        }
    }

    private fun CardPostBinding.likeButtonChange(post: Post) {
        if (post.likeByMe) {
            likeButton.setIconResource(R.drawable.ic_baseline_favorite_24)
            likeButton.setIconTintResource(R.color.colorRed)
        } else {
            likeButton.setIconResource(R.drawable.ic_baseline_favorite_border_24)
            likeButton.setIconTintResource(R.color.colorDarkGrey)
        }
    }
}

class AdViewHolder(
    private val binding: CardAdBinding,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(ad: Ad) {
        with(binding.root) {
            binding.image.load("http://10.0.2.2:9999/media/${ad.image}")

            setOnClickListener {
                onItemClickListener.onAdClick(ad)
            }
        }
    }

}

class FeedDiffCallback : DiffUtil.ItemCallback<FeedItem>() {
    override fun areItemsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
        return oldItem == newItem
    }
}
