import { useEffect, useState, useRef, useCallback } from "react";
import CreatePost from "./CreatePost";
import PostContainer from "./PostsContainer";
import { fetchPosts, fetchSearch } from "../voterraUtils/PostUtils";
import Loader from "./Loader";
import { currFilter, currSearch } from "../recoil/atoms";
import { useRecoilState } from "recoil";

const MainSection = () => {
    const [posts, setPosts] = useState([]);
    const [page, setPage] = useState(0);
    const [isLoading, setIsLoading] = useState(false);
    const [hasMore, setHasMore] = useState(true);
    const observerRef = useRef(null);
    const [filter, setFilter] = useRecoilState(currFilter);
    const [search, setSearch] = useRecoilState(currSearch);

    useEffect(() => {
        const applyFilter = async () => {
            setPage(0);
            setHasMore(true);
            setPosts([]);
            setIsLoading(true);

            try {
                const data = await fetchPosts(filter, 0);
                setPosts(data);
                setHasMore(data.length > 0);
            } catch (error) {
                console.error("Error fetching posts for filter:", error);
            } finally {
                setIsLoading(false);
            }
        };
        applyFilter();
    }, [filter]);

    useEffect(() => {
        const fetchSearchPosts = async () => {
            setPage(0);
            setHasMore(true);
            setPosts([]);
            setIsLoading(true);

            try {
                const data = await fetchSearch(search, 0);
                setPosts(data);
                setHasMore(data.length > 0);
            } catch (error) {
                console.error("Error fetching posts for search:", error);
            } finally {
                setIsLoading(false);
            }
        };

        if (search.trim()) {
            fetchSearchPosts();
        } else {
            setPage(0);
            setHasMore(true);
            setPosts([]);
            setIsLoading(true);
            fetchPosts(filter, 0).then((data) => {
                setPosts(data);
                setHasMore(data.length > 0);
            }).catch((error) => console.error(error)).finally(() => setIsLoading(false));
        }
    }, [search, filter]);

    const loadPosts = useCallback(
        async (currentPage) => {
            if (isLoading || !hasMore) return;

            setIsLoading(true);
            try {
                let data;
                if (search.trim()) {
                    data = await fetchSearch(search, currentPage);
                } else {
                    data = await fetchPosts(filter, currentPage);
                }

                if (data.length > 0) {
                    setPosts((prevPosts) => [...prevPosts, ...data]);
                    setPage(currentPage + 1);
                } else {
                    setHasMore(false);
                }
            } catch (error) {
                console.error("Error loading posts:", error);
            } finally {
                setIsLoading(false);
            }
        },
        [isLoading, hasMore, search, filter]
    );

    useEffect(() => {
        const observer = new IntersectionObserver(
            (entries) => {
                if (entries[0].isIntersecting && hasMore) {
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
    }, [page, hasMore, loadPosts]);

    const addPost = (newPost) => {
        setPosts((prevPosts) => [newPost, ...prevPosts]);
    };

    const removePostFromFeed = (postId) => {
        setPosts((prevPosts) => prevPosts.filter((post) => post.id !== postId));
    };

    return (
        <div className="flex flex-col gap-10">
            <CreatePost addPost={addPost} />
            <PostContainer posts={posts} removePostFromFeed={removePostFromFeed} />
            {isLoading && <Loader />}
            {hasMore && <div ref={observerRef} className="infinite-trigger"></div>}
        </div>
    );
};

export default MainSection;
