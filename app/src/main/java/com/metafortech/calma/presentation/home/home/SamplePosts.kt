package com.metafortech.calma.presentation.home.home

object SamplePosts {
    val samplePosts = listOf(
        // Post with single image
        PostModel(
            id = "1",
            userAvatar = "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=150&h=150&fit=crop&crop=face",
            userName = "خالد الجراح",
            timeAgo = "منذ 20 د",
            content = "اليوم كان يوم رائع! ✨ لأول مرة قدرت أكمل كل شيء من الخطة إلي بدون توقف عن العمل. اقدر لهذا الانجاز واشكر كل اللي شجعوني ووقفوا معي 💯 القادم أفضل إن شاء الله",
            mediaItems = listOf(
                MediaItem(
                    id = "img1",
                    type = MediaType.IMAGE,
                    url = "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800&h=600&fit=crop"
                )
            ),
            hashtags = listOf("#تحفيز", "#الانجاز", "#التدريب", "#نحو_الأفضل"),
            likesCount = 32,
            commentsCount = 24,
            sharesCount = 12,
            isLiked = true
        ),

        // Post with video
        PostModel(
            id = "2",
            userAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&h=150&fit=crop&crop=face",
            userName = "عمر أحمد",
            timeAgo = "منذ ساعة",
            content = "كل خطوة هي خطوة نحو النجاح ✌️",
            mediaItems = listOf(
                MediaItem(
                    id = "vid1",
                    type = MediaType.VIDEO,
                    url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                    thumbnailUrl = "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=800&h=600&fit=crop",
                    duration = "2:15"
                )
            ),
            hashtags = listOf("#فيديو", "#تحفيز"),
            likesCount = 45,
            commentsCount = 18,
            sharesCount = 22,
            isLiked = false
        ),

        // Post with two images
        PostModel(
            id = "3",
            userAvatar = "https://images.unsplash.com/photo-1494790108755-2616b612b786?w=150&h=150&fit=crop&crop=face",
            userName = "فاطمة محمد",
            timeAgo = "منذ 3 ساعات",
            content = "صباح الخير من المكتبة! وقت الدراسة والتركيز 📚✨",
            mediaItems = listOf(
                MediaItem(
                    id = "img2",
                    type = MediaType.IMAGE,
                    url = "https://images.unsplash.com/photo-1481627834876-b7833e8f5570?w=800&h=600&fit=crop"
                ),
                MediaItem(
                    id = "img3",
                    type = MediaType.IMAGE,
                    url = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=800&h=600&fit=crop"
                )
            ),
            hashtags = listOf("#دراسة", "#مكتبة", "#تركيز"),
            likesCount = 67,
            commentsCount = 31,
            sharesCount = 8,
            isLiked = true
        ),

        // Post with three images
        PostModel(
            id = "4",
            userAvatar = "https://images.unsplash.com/photo-1560250097-0b93528c311a?w=150&h=150&fit=crop&crop=face",
            userName = "أحمد علي",
            timeAgo = "منذ 5 ساعات",
            content = "رحلة رائعة مع الأصدقاء اليوم! الطبيعة جميلة والأجواء مثالية 🌿🏔️",
            mediaItems = listOf(
                MediaItem(
                    id = "img4",
                    type = MediaType.IMAGE,
                    url = "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800&h=600&fit=crop"
                ),
                MediaItem(
                    id = "img5",
                    type = MediaType.IMAGE,
                    url = "https://images.unsplash.com/photo-1441974231531-c6227db76b6e?w=800&h=600&fit=crop"
                ),
                MediaItem(
                    id = "img6",
                    type = MediaType.IMAGE,
                    url = "https://images.unsplash.com/photo-1448375240586-882707db888b?w=800&h=600&fit=crop"
                )
            ),
            hashtags = listOf("#رحلة", "#طبيعة", "#أصدقاء"),
            likesCount = 89,
            commentsCount = 42,
            sharesCount = 15,
            isLiked = false
        ),

        // Post with multiple images (5 images)
        PostModel(
            id = "5",
            userAvatar = "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=150&h=150&fit=crop&crop=face",
            userName = "سارة الأحمد",
            timeAgo = "منذ يوم",
            content = "معرض الفن المحلي كان مذهلاً! شكراً لكل الفنانين على الإبداع الرائع 🎨",
            mediaItems = listOf(
                MediaItem(
                    id = "img7",
                    type = MediaType.IMAGE,
                    url = "https://images.unsplash.com/photo-1578662996442-48f60103fc96?w=800&h=600&fit=crop"
                ),
                MediaItem(
                    id = "img8",
                    type = MediaType.IMAGE,
                    url = "https://images.unsplash.com/photo-1578662996446-a65c7b6740b2?w=800&h=600&fit=crop"
                ),
                MediaItem(
                    id = "img9",
                    type = MediaType.IMAGE,
                    url = "https://images.unsplash.com/photo-1578662996443-08ecb0f1adeb?w=800&h=600&fit=crop"
                ),
                MediaItem(
                    id = "img10",
                    type = MediaType.IMAGE,
                    url = "https://images.unsplash.com/photo-1579952363873-27d3bfad9c0d?w=800&h=600&fit=crop"
                ),
                MediaItem(
                    id = "img11",
                    type = MediaType.IMAGE,
                    url = "https://images.unsplash.com/photo-1582201943021-e0ce3ba96307?w=800&h=600&fit=crop"
                )
            ),
            hashtags = listOf("#فن", "#معرض", "#إبداع", "#ثقافة"),
            likesCount = 156,
            commentsCount = 73,
            sharesCount = 28,
            isLiked = true
        ),

        // Post with audio
        PostModel(
            id = "6",
            userAvatar = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150&h=150&fit=crop&crop=face",
            userName = "محمد الطيب",
            timeAgo = "منذ يومين",
            content = "تسجيل صوتي قصير عن تجربتي في التأمل وأهميته في الحياة اليومية 🧘‍♂️",
            mediaItems = listOf(
                MediaItem(
                    id = "audio1",
                    type = MediaType.AUDIO,
                    url = "https://www.soundjay.com/misc/sounds-1159.mp3",
                    duration = "3:42"
                )
            ),
            hashtags = listOf("#تأمل", "#صحة_نفسية", "#تسجيل_صوتي"),
            likesCount = 78,
            commentsCount = 34,
            sharesCount = 19,
            isLiked = false
        ),

        // Text-only post
        PostModel(
            id = "7",
            userAvatar = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=150&h=150&fit=crop&crop=face",
            userName = "نور الهدى",
            timeAgo = "منذ 3 أيام",
            content = "أحياناً أفضل الأوقات هي تلك البسيطة التي نقضيها مع أنفسنا. قراءة كتاب، شرب الشاي، والاستمتاع بالصمت. السعادة في التفاصيل الصغيرة ✨📖☕",
            mediaItems = emptyList(),
            hashtags = listOf("#تأمل", "#سعادة", "#بساطة", "#قراءة"),
            likesCount = 234,
            commentsCount = 87,
            sharesCount = 45,
            isLiked = true
        ),

        // Mixed media post (image + video)
        PostModel(
            id = "8",
            userAvatar = "https://images.unsplash.com/photo-1507591064344-4c6ce005b128?w=150&h=150&fit=crop&crop=face",
            userName = "يوسف كريم",
            timeAgo = "منذ أسبوع",
            content = "ورشة عمل اليوم كانت مفيدة جداً! تعلمنا مهارات جديدة وتبادلنا الخبرات 💡",
            mediaItems = listOf(
                MediaItem(
                    id = "img12",
                    type = MediaType.IMAGE,
                    url = "https://images.unsplash.com/photo-1552664730-d307ca884978?w=800&h=600&fit=crop"
                ),
                MediaItem(
                    id = "vid2",
                    type = MediaType.VIDEO,
                    url = "https://sample-videos.com/zip/10/mp4/SampleVideo_640x360_1mb.mp4",
                    thumbnailUrl = "https://images.unsplash.com/photo-1553877522-43269d4ea984?w=800&h=600&fit=crop",
                    duration = "1:30"
                ),
                MediaItem(
                    id = "img13",
                    type = MediaType.IMAGE,
                    url = "https://images.unsplash.com/photo-1517245386807-bb43f82c33c4?w=800&h=600&fit=crop"
                ),
                MediaItem(
                    id = "img14",
                    type = MediaType.IMAGE,
                    url = "https://images.unsplash.com/photo-1542744173-8e7e53415bb0?w=800&h=600&fit=crop"
                )
            ),
            hashtags = listOf("#ورشة_عمل", "#تعلم", "#مهارات", "#تطوير"),
            likesCount = 198,
            commentsCount = 65,
            sharesCount = 33,
            isLiked = false
        )
    )
}