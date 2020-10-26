package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.Utils

typealias OnLikeListener = (post: Post) -> Unit
typealias OnShareListener = (post: Post) -> Unit

class PostAdapter(
    private val onLikeListener: OnLikeListener,
    private var onShareListener: OnShareListener
) : RecyclerView.Adapter<PostViewHolder>() {

    var list = emptyList<Post>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onLikeListener, onShareListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = list[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int = list.size

}

class PostViewHolder (
    private val binding: CardPostBinding,
    private val onLikeListener: OnLikeListener,
    private val onShareListener: OnShareListener

) : RecyclerView.ViewHolder(binding.root) {

    fun bind (post: Post) {
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
            likeButton.setOnClickListener {
                onLikeListener(post)
            }
            toShareButton.setOnClickListener {
                onShareListener(post)
            }
        }
    }
}