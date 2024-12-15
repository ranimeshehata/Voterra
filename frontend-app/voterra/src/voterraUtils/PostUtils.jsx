export const createPost=(content,polls,category,privacy,username,email)=>{
    const today = new Date();
    const formattedDate = today.toISOString().split('T')[0];
    let postObj={};
    postObj.postContent = content;
    postObj.userEmail = email;
    postObj.category = category;
    postObj.privacy = privacy;
    postObj.userName = username;
    postObj.publishedDate=formattedDate;
    postObj.polls=[];
    for(let i=0; i<polls.length; i++){
        let temp={
            pollContent:polls[i],
            voters:[]
        }
        postObj.polls.push(temp);
    }
}