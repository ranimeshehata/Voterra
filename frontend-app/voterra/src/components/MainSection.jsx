import CreatePost from "./CreatePost";
import PostContainer from "./PostsContainer";

const MainSection = () => {
    return ( 
        <div className="flex flex-col gap-10">
            <CreatePost/>
            <PostContainer/>
        </div>
     );
}
 
export default MainSection;