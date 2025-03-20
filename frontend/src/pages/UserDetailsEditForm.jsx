import {useState} from "react";
import axiosInstance from "../context/AxiosInstance.jsx";


export default function UserDetailsEditForm({oldBio, handleSubmit, handleCancel}) {
    const [bio, setBio] = useState(oldBio);
    const [profilePicture, setProfilePicture] = useState(null);

    function handleProfilePictureChange(e) {
        setProfilePicture(e.target.files[0]);
    }


    return (
        <div className="flex flex-col items-center justify-center w-full max-w-md mx-auto">
            <form onSubmit={(e)=>handleSubmit(e, bio, profilePicture)}>
                <label htmlFor="bio">Bio</label>
                <input
                    className="w-full py-2 pl-10 pr-4 text-gray-700 bg-white border rounded-md dark:bg-gray-800 dark:text-gray-300 dark:border-gray-600 focus:border-blue-400 dark:focus:border-blue-300 focus:outline-none focus:ring focus:ring-opacity-40 focus:ring-blue-300"
                    type="text"
                    placeholder={bio}
                    value={bio}
                    onChange={e => setBio(e.target.value)}
                />
                <label htmlFor="profilePicture">Profile Picture</label>
                <input
                    className="w-full py-2 pl-4 pr-4 text-gray-700 bg-white border rounded-md dark:bg-gray-800 dark:text-gray-300 dark:border-gray-600 focus:border-blue-400 dark:focus:border-blue-300 focus:outline-none focus:ring focus:ring-opacity-40 focus:ring-blue-300 file:mr-4 file:py-2 file:px-4 file:rounded-md file:border-0 file:text-sm file:font-semibold file:bg-blue-50 file:text-blue-700 hover:file:bg-sky-500 dark:file:bg-gray-700 dark:file:text-gray-200"
                    id="profilePicture"
                    name="profilePicture"
                    type="file"
                    accept="image/*"
                    onChange={handleProfilePictureChange}
                />
                <button
                    className="w-full rounded-md bg-sky-500 px-4 py-2 text-sm font-medium text-white hover:bg-sky-600 focus:outline-none focus:ring-2 focus:ring-sky-500 focus:ring-offset-2 dark:bg-sky-400 dark:text-sky-900 dark:hover:bg-sky-300 dark:focus:ring-sky-400"
                    type="submit"
                >
                    Submit
                </button>
                <button
                    className="w-full rounded-md bg-sky-500 px-4 py-2 text-sm font-medium text-white hover:bg-sky-600 focus:outline-none focus:ring-2 focus:ring-sky-500 focus:ring-offset-2 dark:bg-sky-400 dark:text-sky-900 dark:hover:bg-sky-300 dark:focus:ring-sky-400"
                    type="button"
                    onClick={handleCancel}
                >
                    Cancel
                </button>
            </form>
        </div>
    )

}