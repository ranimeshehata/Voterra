import { useEffect, useState, useRef, useCallback } from "react";
import CreatePost from "./CreatePost";
import PostContainer from "./PostsContainer";
import { fetchPosts } from "../voterraUtils/PostUtils";
import Loader from "./Loader";

const MainSection = () => {
    const [posts, setPosts] = useState([]);
    const [page, setPage] = useState(0);
    const [isLoading, setIsLoading] = useState(false);
    const [hasMore, setHasMore] = useState(true);
    const observerRef = useRef(null);

    const loadPosts = useCallback(async (page) => {
        if (isLoading || !hasMore) return;
        setIsLoading(true);
        try {
            const data = await fetchPosts(page);
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

    const addPost = (newPost) => {
        setPosts([newPost, ...posts]);
    };

    const removePostFromFeed = (postId) => {
        setPosts(posts.filter(post => post.id !== postId));
    };

    return (
        <div className="flex flex-col gap-10">
            <CreatePost addPost={addPost} />
            <PostContainer posts={posts} removePostFromFeed = { removePostFromFeed } />
            {isLoading && <Loader/>}
            {hasMore && <div ref={observerRef} className="infinite-trigger"></div>}
        </div>
    );
};

export default MainSection;
