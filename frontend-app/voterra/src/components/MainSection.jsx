/* eslint-disable no-unused-vars */
import { useEffect, useState, useRef } from "react";
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
    const loadPosts = async (page) => {
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
            console.error("Error fetching posts:", error);
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        loadPosts(page);//first one
    }, []); 
    useEffect(() => {
        const observer = new IntersectionObserver(
            (entries) => {
                if (entries[0].isIntersecting) {
                    loadPosts(page);
                }
            },
            { threshold: 1.0 }
        );
        if (observerRef.current) observer.observe(observerRef.current);

        return () => {
            if (observerRef.current) observer.unobserve(observerRef.current);
        };
    }, [page, hasMore, isLoading]);

    const addPost = (newPost) => {
        setPosts([newPost, ...posts]);
    };

    return (
        <div className="flex flex-col gap-10">
            <CreatePost addPost={addPost} />
            <PostContainer posts={posts} />
            {isLoading && <Loader/>}
            {hasMore && <div ref={observerRef} className="infinite-trigger"></div>}
        </div>
    );
};

export default MainSection;
