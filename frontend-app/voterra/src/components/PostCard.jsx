import { useEffect, useState } from "react";

const PostCard = ({post}) => {
    const [totalVotes,setTotalVotes]=useState(0);
    useEffect(()=>{
        let x=0;
        for(let i=0;i<post.polls.length;i++){
            x+=post.polls[i].voters.length;
        }
        setTotalVotes(x);
    },[])
    return ( 
        <div className="shadow-xl rounded-lg p-5 flex flex-col gap-3 font-[nunito]">
            <div id="userInfo" className="flex gap-3 items-center">
                <div className="w-10 h-10 rounded-full flex bg-red-500 text-white justify-center items-center">
                    {post.userName[0]}
                </div>
                <p>posted by {post.userName} &#9679; {post.publishedDate}</p>
            </div>

            <h1 className="text-2xl">{post.postContent}</h1>

            <div id="polls" className="flex flex-col gap-4">
                {post.polls.map((poll)=>(
                    <div className="p-2 shadow-lg rounded-lg bg-gray-100">
                        <h2>{poll.pollContent}</h2>
                    </div>
                ))}
            </div>
            <p>{totalVotes} voters</p>
            <div>
                <p className="p-2 bg-gray-200 rounded-full w-fit">{post.category}</p>
            </div>
        </div>
     );
}
 
export default PostCard;