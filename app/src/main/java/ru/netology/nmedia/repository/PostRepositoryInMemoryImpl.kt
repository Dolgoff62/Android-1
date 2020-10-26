package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.R
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemoryImpl : PostRepository {

    private var post = Post(
        id = 1,
        avatar = R.drawable.netology,
        author = "Нетология. Университет интернет-профессий будущего",
        content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer a turpis orci. Fusce metus felis, tristique at neque eget, accumsan sollicitudin augue. Nullam mi lectus, eleifend sit amet arcu a, accumsan volutpat massa. Curabitur euismod sem sed egestas hendrerit. In at arcu gravida, pretium lectus non, pulvinar velit. Mauris viverra eu massa nec eleifend. Pellentesque semper convallis facilisis. Suspendisse eu venenatis lorem. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Maecenas ultricies ultrices aliquam. Nullam vel efficitur quam, vitae placerat nisi. Ut pellentesque dui in turpis fermentum vehicula. Curabitur congue, dolor non hendrerit rhoncus, mi elit euismod ex, interdum finibus quam justo ut urna. Cras aliquam tristique ex nec dapibus. Phasellus volutpat tellus nec ipsum fringilla, vel tincidunt sapien egestas. Nulla ut ipsum venenatis, posuere est in, tincidunt odio. Aliquam erat volutpat. Aliquam hendrerit ac eros id dapibus. Vivamus ut nulla mattis, maximus sem sit amet, cursus neque. Aenean pharetra tempus sapien, et luctus lacus tincidunt ut. Vivamus ut odio vitae enim suscipit sagittis. Nulla nec aliquam nisl. Cras nec dui non lorem maximus aliquet non sit amet nisl. Nunc nec lectus magna. Praesent lacus mi, dictum id consequat ut, cursus id lorem. Nulla lacinia vulputate. -> https://ru.lipsum.com/",
        published = "2 сентября в 18:30",
        likeByMe = false,
        numberOfLikes = 999_999_999,
        numberOfShare = 1,
        numberOfViews = 1_500_000
    )

    private val data = MutableLiveData(post)

    override fun get(): LiveData<Post> = data

    override fun like() {
        post = post.copy(likeByMe = !post.likeByMe)
        post = if (post.likeByMe) {
            post.copy(numberOfLikes = post.numberOfLikes + 1)
        } else {
            post.copy(numberOfLikes = post.numberOfLikes - 1)
        }
        data.value = post
    }

    override fun toShare() {
        post = post.copy(numberOfShare = post.numberOfShare + 1)
        data.value = post
    }
}
