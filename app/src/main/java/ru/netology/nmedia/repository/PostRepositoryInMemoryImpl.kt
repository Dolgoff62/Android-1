package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.R
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemoryImpl : PostRepository {

    private var posts = listOf(
        Post(
            id = 1,
            avatar = R.drawable.netology,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer a turpis orci. Fusce metus felis, tristique at neque eget, accumsan sollicitudin augue. Nullam mi lectus, eleifend sit amet arcu a, accumsan volutpat massa. Curabitur euismod sem sed egestas hendrerit. In at arcu gravida, pretium lectus non, pulvinar velit. Mauris viverra eu massa nec eleifend. Pellentesque semper convallis facilisis. Suspendisse eu venenatis lorem. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Maecenas ultricies ultrices aliquam. Nullam vel efficitur quam, vitae placerat nisi. Ut pellentesque dui in turpis fermentum vehicula. Curabitur congue, dolor non hendrerit rhoncus, mi elit euismod ex, interdum finibus quam justo ut urna. Cras aliquam tristique ex nec dapibus. Phasellus volutpat tellus nec ipsum fringilla, vel tincidunt sapien egestas. Nulla ut ipsum venenatis, posuere est in, tincidunt odio. Aliquam erat volutpat. Aliquam hendrerit ac eros id dapibus. Vivamus ut nulla mattis, maximus sem sit amet, cursus neque. Aenean pharetra tempus sapien, et luctus lacus tincidunt ut. Vivamus ut odio vitae enim suscipit sagittis. Nulla nec aliquam nisl. Cras nec dui non lorem maximus aliquet non sit amet nisl. Nunc nec lectus magna. Praesent lacus mi, dictum id consequat ut, cursus id lorem. Nulla lacinia vulputate. -> https://ru.lipsum.com/",
            published = "2 сентября в 18:30",
            likeByMe = false,
            numberOfLikes = 999_999_999,
            numberOfShare = 1,
            numberOfViews = 1_500_000
        ),
        Post(
            id = 2,
            avatar = R.drawable.netology,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer a turpis orci. Fusce metus felis, tristique at neque eget, accumsan sollicitudin augue. Nullam mi lectus, eleifend sit amet arcu a, accumsan volutpat massa. Curabitur euismod sem sed egestas hendrerit. In at arcu gravida, pretium lectus non, pulvinar velit. Mauris viverra eu massa nec eleifend. Pellentesque semper convallis facilisis. Suspendisse eu venenatis lorem. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Maecenas ultricies ultrices aliquam. Nullam vel efficitur quam, vitae placerat nisi. Ut pellentesque dui in turpis fermentum vehicula. Curabitur congue, dolor non hendrerit rhoncus, mi elit euismod ex, interdum finibus quam justo ut urna. Cras aliquam tristique ex nec dapibus. Phasellus volutpat tellus nec ipsum fringilla, vel tincidunt sapien egestas. Nulla ut ipsum venenatis, posuere est in, tincidunt odio. Aliquam erat volutpat. Aliquam hendrerit ac eros id dapibus. Vivamus ut nulla mattis, maximus sem sit amet, cursus neque. Aenean pharetra tempus sapien, et luctus lacus tincidunt ut. Vivamus ut odio vitae enim suscipit sagittis. Nulla nec aliquam nisl. Cras nec dui non lorem maximus aliquet non sit amet nisl. Nunc nec lectus magna. Praesent lacus mi, dictum id consequat ut, cursus id lorem. Nulla lacinia vulputate. -> https://ru.lipsum.com/",
            published = "2 сентября в 19:45",
            likeByMe = false,
            numberOfLikes = 12,
            numberOfShare = 5,
            numberOfViews = 1_000
        ),
        Post(
            id = 3,
            avatar = R.drawable.netology,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?",
            published = "3 сентября в 9:45",
            likeByMe = false,
            numberOfLikes = 50,
            numberOfShare = 11,
            numberOfViews = 1_045
        ),
        Post(
            id = 4,
            avatar = R.drawable.netology,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga. Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omnis dolor repellendus. Temporibus autem quibusdam et aut officiis debitis aut rerum necessitatibus saepe eveniet ut et voluptates repudiandae sint et molestiae non recusandae. Itaque earum rerum hic tenetur a sapiente delectus, ut aut reiciendis voluptatibus maiores alias consequatur aut perferendis doloribus asperiores repellat.",
            published = "5 сентября в 15:00",
            likeByMe = false,
            numberOfLikes = 5,
            numberOfShare = 10,
            numberOfViews = 900
        ),
        Post(
            id = 5,
            avatar = R.drawable.myavatar,
            author = "Дмитрий Долгов",
            content = "On the other hand, we denounce with righteous indignation and dislike men who are so beguiled and demoralized by the charms of pleasure of the moment, so blinded by desire, that they cannot foresee the pain and trouble that are bound to ensue; and equal blame belongs to those who fail in their duty through weakness of will, which is the same as saying through shrinking from toil and pain. These cases are perfectly simple and easy to distinguish. In a free hour, when our power of choice is untrammelled and when nothing prevents our being able to do what we like best, every pleasure is to be welcomed and every pain avoided. But in certain circumstances and owing to the claims of duty or the obligations of business it will frequently occur that pleasures have to be repudiated and annoyances accepted. The wise man therefore always holds in these matters to this principle of selection: he rejects pleasures to secure other greater pleasures, or else he endures pains to avoid worse pains.",
            published = "12 сентября в 15:30",
            likeByMe = false,
            numberOfLikes = 777,
            numberOfShare = 754,
            numberOfViews = 1_000_000
        )
    )

    private val data = MutableLiveData(posts)

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) {
                it
            } else {
                it.copy(
                    likeByMe = !it.likeByMe,
                    numberOfLikes = if (it.likeByMe) it.numberOfLikes - 1 else it.numberOfLikes + 1
                )
            }
        }
        data.value = posts
    }

    override fun toShareById(id: Long) {
        posts = posts.map {
            if (it.id != id) {
                it
            } else {
                it.copy(numberOfShare = it.numberOfShare + 1)
            }
        }
        data.value = posts
    }
}
