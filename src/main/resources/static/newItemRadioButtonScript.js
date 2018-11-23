function WhichCheck() {
    
    if (document.getElementById('book').checked) {
        document.getElementById('bookAdd').style.display = 'block';
        document.getElementById('blogAdd').style.display = 'none';
        document.getElementById('videoAdd').style.display = 'none';
        
    } else if (document.getElementById('blog').checked) {
        document.getElementById('blogAdd').style.display = 'block';
        document.getElementById('bookAdd').style.display = 'none';
        document.getElementById('videoAdd').style.display = 'none';
        
    } else {
        document.getElementById('videoAdd').style.display = 'block';
        document.getElementById('bookAdd').style.display = 'none';
        document.getElementById('blogAdd').style.display = 'none';
    }
}


