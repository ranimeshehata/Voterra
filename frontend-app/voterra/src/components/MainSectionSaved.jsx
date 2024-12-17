import { useEffect, useState, useRef, useCallback } from "react";
import PostContainer from "./PostsContainer";
import { fetchSavedPosts } from "../voterraUtils/PostUtils";
import Loader from "./Loader";

const MainSectionSaved = () => {
    const [posts, setPosts] = useState([]);
    const [page, setPage] = useState(0);
    const [isLoading, setIsLoading] = useState(false);
    const [hasMore, setHasMore] = useState(true);
    const observerRef = useRef(null);

    const loadPosts = useCallback(async (page) => {
        if (isLoading || !hasMore) return;
        setIsLoading(true);
        try {
            console.log("fetching saved posts");
            const data = await fetchSavedPosts(page);
            if (data.length > 0) {
                setPosts((prevPosts) => [...prevPosts, ...data]);
                setPage(page + 1);
                console.log("posts", posts);
            } else {
                setHasMore(false);
            }
        } catch (error) {
            console.error("Error fetching posts:", error);
        } finally {
            setIsLoading(false);
        }
    }, [isLoading, hasMore, posts]);
 
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
        </div>
    );
};

export default MainSectionSaved;
