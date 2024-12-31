

export const createPost=async(content,polls,category,privacy,username,email)=>{
  const today = new Date();
  const formattedDate = today.toISOString();
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
    const token = localStorage.getItem("token");
}
export async function fetchUserProfilePosts(page,email) {
  const token = localStorage.getItem("token");
  try {
    const response = await fetch(`http://localhost:8080/posts/userContent?email=${email}&page=${page}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization:"Bearer "+ token
      },
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'failed');
    }
    const data = await response.json();
    return data;
  } catch (error) {
    console.error(error.message);
  }
}

export async function fetchPosts(filter,page) {
    const token = localStorage.getItem("token");
    try {
      const response = await fetch(`http://localhost:8080/posts/homepage?category=${filter}&page=${page}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          Authorization:"Bearer "+ token
        },
        
      });
  
      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'failed');
      }
      const data = await response.json();
      return data;
    } catch (error) {
      console.error(error.message);
    }
}

export async function fetchSavedPosts(page) {
    const token = localStorage.getItem("token");
    try {
      const response = await fetch(`http://localhost:8080/posts/getSavedPosts?page=${page}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          Authorization:"Bearer "+ token
        },
      });
  
      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'failed');
      }
  
      const data = await response.json();
      return data;
    } catch (error) {
      console.error(error.message);
    }
}
export const fetchSearch = async ( search, page) => {
  const token = localStorage.getItem("token");
  try {
      const response = await fetch(`http://localhost:8080/posts/search?postContent=${encodeURIComponent(search)}&page=${page}`, {
          method: "GET",
          headers: {
              "Content-Type": "application/json",
              Authorization:"Bearer "+ token
          },
      });

      if (!response.ok) {
          const error = await response.json();
          throw new Error(error.message || "Failed to fetch posts");
      }

      const data = await response.json();
      return data;
  } catch (error) {
      console.error("Error fetching posts:", error.message);
      return [];
  }
};
