import { useState } from "react";
import { useRecoilState } from "recoil";
import { currFilter, currSearch } from "../recoil/atoms";

const SearchBar = () => {
    const categories = [
        "all","SPORTS", "TECHNOLOGY", "ENTERTAINMENT", "HEALTH", "EDUCATION",
        "BUSINESS", "FASHION", "FOOD", "JOBS", "MEDICAL", "CARS", "EVENTS",
        "PLACES", "CELEBRITIES", "NATURE", "MOVIES", "OTHER"
    ];    
    const [search, setSearch] = useRecoilState(currSearch);
    const [filter,setFilter]=useRecoilState(currFilter);
    const handleInputChange = (e) => {
        setSearch(e.target.value);
    };
      const handleSelectChange = (e) => {
        console.log("uyy");
        
        setFilter(e.target.value);
    };
    return ( 
        <div className="flex gap-0">
            <select onChange={handleSelectChange} value={filter} className="border-2 shadow-2xl rounded-l-full p-2" name="" id="">
                {categories.map((category)=>(
                    <option value={category}>{category}</option>
                ))}
            </select>
            <input value={search} onChange={handleInputChange}  placeholder="Search some posts...." className="w-80 shadow-2xl border-r-2 border-t-2 border-b-2 rounded-r-full  p-2" type="text" />
        </div>
     );
}
 
export default SearchBar;