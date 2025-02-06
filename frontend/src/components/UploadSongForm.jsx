import {useState} from "react";
import axios from "axios";

export default function UploadSongForm() {
    const [title, setTitle] = useState('');
    const [file, setFile] = useState(null);
    //REPLACE WITH NORMAL USER ID
    const authorId = "Dr. Assman"

    function handleFileChange(e) {
        setFile(e.target.files[0]);
    }

    async function handleSubmit(e) {
        e.preventDefault();
        if(!file || !title){
            alert("Please fill in all fields.");
            return;
        }
        const formData = new FormData();
        formData.append("file", file)
        formData.append("title", title);
        formData.append("length", 100);
        formData.append("author", authorId)

        try{
            await axios.post("http://localhost:8080/api/song/upload", formData, {
                headers: { "Content-Type": "multipart/form-data" }
            })
        } catch (error){
            console.error(error);
            alert("Upload Failed!");
        }
    }

    return (
        <form onSubmit={handleSubmit}>
            <label htmlFor={"title"}>Title: </label>
            <input id={"title"} name={"title"} type={"text"} onChange={(e) => setTitle(e.target.value)} />
            <label htmlFor={"audio"}>Song: </label>
            <input id={"audio"} name={"audio"} type={"file"} onChange={handleFileChange} />
            <button type="submit">Submit</button>
        </form>
    )
}
