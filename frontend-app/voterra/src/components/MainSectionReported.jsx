import { useEffect, useState, useRef, useCallback } from "react";
import PostContainer from "./PostsContainer";
import { fetchReportedPosts } from "../voterraUtils/PostUtils";
import Loader from "./Loader";

function MainSectionReported() {
    const [posts, setPosts] = useState([]);
    const [page, setPage] = useState(0);
    const [isLoading, setIsLoading] = useState(false);
    const [hasMore, setHasMore] = useState(true);
    const observerRef = useRef(null);

    const loadPosts = useCallback(async (page) => {
        if (isLoading || !hasMore) return;
        setIsLoading(true);
        try {
            const data = await fetchReportedPosts(page);
            console.log(data);
            if (data.length > 0) {
                setPosts((prevPosts) => [...prevPosts, ...data]);
                setPage(page + 1);
            } else {
                setHasMore(false);
            }
        } catch (error) {
            console.error(error);
        } finally {
            setIsLoading(false);
        }
    }, [isLoading, hasMore]);
 
    useEffect(() => {
        const observer = new IntersectionObserver(
            (entries) => {
                if (entries[0].isIntersecting) {
                    loadPosts(page);
                }
            },
            { threshold: 1.0 }
        );
        const currentObserverRef = observerRef.current;
        if (currentObserverRef) observer.observe(currentObserverRef);

        return () => {
            if (currentObserverRef) observer.unobserve(currentObserverRef);
        };
    }, [page, hasMore, isLoading, loadPosts]);

    const removePostFromFeed = (postId) => {
        setPosts(posts.filter(post => post.id !== postId));
    };

    const handleSavePost = (postId) => {
        setPosts(posts.map(post => 
          post.id === postId ? { ...post, isSaved: true } : post
        ));
      };

    return (
        <div className="flex flex-col gap-10">
            <PostContainer posts={posts} removePostFromFeed = { removePostFromFeed } onSavePost={handleSavePost} />
            {isLoading && <Loader/>}
            {hasMore && <div ref={observerRef} className="infinite-trigger"></div>}
            
            {!isLoading && (
            <>
                {posts.length === 0 && <div className="text-center text-4xl text-gray-600">No reported posts yet ..</div>}
                <div className="flex justify-center">
                    <button
                        className="bg-red-500 mb-5 text-white p-2 rounded-lg mt-2 shadow"
                        onClick={() => window.location.href = "/homepage"}
                    >
                        Back to Home
                    </button>
                </div>
            </>
        )}
        </div>
    );

}

export default MainSectionReported;