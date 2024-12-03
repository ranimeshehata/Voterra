import { useState } from "react";

function useFetch() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchData = async (url, options = {}, onComplete, onError) => {
    setLoading(true);
    setError(null);
    try {
      console.log("Fetching:", url, options);
      const response = await fetch(url, { ...options, mode: "cors" });
      console.log("Response:", response);
  
      const result = response.headers.get("Content-Type")?.includes("application/json")
        ? await response.json()
        : await response.text(); // Handle plain text responses
      
      console.log("Result:", result);
      setData(result);
      if (onComplete) {
        onComplete(result, null);
      }
    } catch (err) {
      console.error("Error:", err);
      setError(err.message);
      if (onError) {
        onError();
      }
      if (onComplete) {
        onComplete(null, err);
      }
    } finally {
      setLoading(false);
    }
  };
  
  
  

  const get = (url, onComplete) => {
    fetchData(url, { method: "GET" }, onComplete);
  };

  const post = (url, body, onComplete,onError) => {
    fetchData(
      url,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(body),
      },
      onComplete,onError
    );
  };

  const postSignout = (url, options = {}, onComplete, onError) => {
    console.log(options);
    console.log(options.headers.Authorization);
    fetchData(
      url,
      {
        method: "POST",
        mode: "cors",
        credentials: 'include',
        headers: {
        "Content-Type": "application/json",
        "Authorization": options.headers.Authorization,
      },
      },
      onComplete,
      onError
    );
  };
  

  return { data, loading, error, get, post, postSignout };
}

export default useFetch;