function WhichCheck() {

    if (document.getElementById("book").checked) {
        document.getElementById("bookAdd").style.display = "block";
        document.getElementById("blogAdd").style.display = "none";
        document.getElementById("videoAdd").style.display = "none";

    } else if (document.getElementById("blog").checked) {
        document.getElementById("blogAdd").style.display = "block";
        document.getElementById("bookAdd").style.display = "none";
        document.getElementById("videoAdd").style.display = "none";

    } else {
        document.getElementById("videoAdd").style.display = "block";
        document.getElementById("bookAdd").style.display = "none";
        document.getElementById("blogAdd").style.display = "none";
    }
}

//function ItemViewContentCheck() {   //checkbox view implementation if ever needed. Shows all selected checkboxes.
//    if (document.getElementById("books").checked) {
//        document.getElementById("allBooks").style.display = "block";
//        document.getElementById("allItems").style.display = "none";
//    }else{
//        document.getElementById("allBooks").style.display = "none";
//    }
//    if (document.getElementById("blogs").checked) {
//        document.getElementById("allBlogs").style.display = "block";
//        document.getElementById("allItems").style.display = "none";
//    } else {
//        document.getElementById("allBlogs").style.display = "none";
//    }
//    if (document.getElementById("videos").checked) {
//        document.getElementById("allVideos").style.display = "block";
//        document.getElementById("allItems").style.display = "none";
//    } else{
//        document.getElementById("allVideos").style.display = "none";
//    }
//    if (!document.getElementById("videos").checked && !document.getElementById("blogs").checked && !document.getElementById("books").checked){
//        document.getElementById("allBooks").style.display = "none";
//        document.getElementById("allVideos").style.display = "none";
//        document.getElementById("allBlogs").style.display = "none";
//        document.getElementById("allItems").style.display = "block";
//    }
//}


