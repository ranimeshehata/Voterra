/* eslint-disable no-unused-vars */
import { Scrollbars } from 'react-custom-scrollbars-2';
import { useState } from 'react';

function UserFeed() {
  const [newPostTitle, setNewPostTitle] = useState("");
  const [newPostQuestion, setNewPostQuestion] = useState("");
  const [posts, setPosts] = useState([{
    id: 1,
    author: "sarahj",
    date: "3/20/2024",
    title: "Your Favorite Frontend Framework",
    question: "Which frontend framework do you prefer for building modern web applications?",
    options: ["React", "Vue", "Angular", "Svelte"],
    votes: 345,
    category: ["programming"],
  },
  {
    id: 2,
    author: "mikedev",
    date: "3/19/2024",
    title: "The Ultimate Guide to Modern CSS",
    question: "",
    options: ["Flexbox", "Grid", "Animations", "Responsive Design"],
    votes: 156,
    category: ["programming"],
  },
  {
    id: 3,
    author: "johndoe",
    date: "3/18/2024",
    title: "Best Practices for RESTful APIs",
    question: "What are the best practices for designing RESTful APIs?",
    options: ["Versioning", "Pagination", "Error Handling", "Security"],
    votes: 234,
    category: ["programming"],
  },
  {
    id: 4,
    author: "janedev",
    date: "3/17/2024",
    title: "JavaScript Frameworks Comparison",
    question: "Which JavaScript framework is best suited for large-scale applications?",
    options: ["React", "Angular", "Vue", "Ember"],
    votes: 456,
    category: ["programming"],
  }]);

  
  const [menuOpen, setMenuOpen] = useState(null);

   const handleSavePost = (postId) => {
  };

  const toggleMenu = (postId) => {
    setMenuOpen(menuOpen === postId ? null : postId);
  };


  return (
    <div>
      <div className="create-post">
        <h3>Create a Post</h3>
        <textarea
          placeholder="Ask a question or share something..."
          // value={newPostQuestion}
          // onChange={(e) => setNewPostQuestion(e.target.value)}
          className="textarea-field"
        ></textarea>
        <button 
          // onClick={handleCreatePost} 
          className="create-button"
        >
          Post
        </button>
      </div>
   
    <div className="user-feed">
       <Scrollbars
            style={{
              width: '100%',
              height: '100vh'
            }}
          >
      {posts.map((post) => (
          <div key={post.id} className="post-card">
            <div className="post-header">
              <span className="author">Posted by {post.author}</span>
              <span className="date">• {post.date}</span>
              <div className="options">
                <button className="options-button" onClick={() => toggleMenu(post.id)}>
                  <span className="dots">&#x2026;</span>
                </button>
                {menuOpen === post.id && (
                  <div className="options-menu">
                    <button onClick={() => handleSavePost(post.id)}>Save Post</button>
                    <button>Delete Post</button>
                  </div>
                )}
              </div>
            </div>

          <h3 className="post-title">{post.title}</h3>
          {post.question && <p className="post-question">{post.question}</p>}
          {post.options && (
            <ul className="post-options">
              {post.options.map((option, index) => (
                <li key={index}>{option}</li>
              ))}
            </ul>
          )}
          <div className="post-footer">
            <span className="votes">{post.votes} votes</span>
            <div className="tags">
                <span className="tag">{post.category}</span>
            </div>
          </div>
        </div>
      ))}
      </Scrollbars>
      </div>
      </div>
  );
}

export default UserFeed;
