<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add New Product</title>
    <link rel="stylesheet" href="../assets/css/bootstrap.css">
    <link rel="stylesheet" href="../assets/css/main.css">
    <link rel="stylesheet" href="../assets/css/all.css">
    <link rel="stylesheet" href="../assets/css/style.css">
    <style>
        /* Loader container styles */
        .loader-container {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(255, 255, 255, 0.8);
            display: flex;
            justify-content: center;
            align-items: center;
            z-index: 1000;
            visibility: hidden; /* Initially hidden */
        }

        /* Loader (spinner) styles */
        .loader {
            border: 16px solid #f3f3f3;
            border-radius: 50%;
            border-top: 16px solid #3498db;
            width: 120px;
            height: 120px;
            animation: spin 2s linear infinite;
        }

        /* Spinner animation */
        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }
    </style>
</head>

<body>

<c:import url="admin-header.jsp" />

<section class="container section_gap">
    <div class="row">
        <div class="col-lg-6">
            <br>
            <br>
            <h3>Add New Product</h3>
            <form class="row login_form" id="addProductForm">
                <!-- Product Basic Info Fields -->
                <div class="col-md-12 form-group">
                    <label for="name">Product Name</label>
                    <input type="text" class="form-control" id="name" name="name" placeholder="Product Name" required>
                </div>
                <div class="col-md-12 form-group">
                    <label for="price">Price</label>
                    <input type="number" class="form-control" id="price" name="price" placeholder="Price" required>
                </div>
                <div class="col-md-12 form-group">
                    <label for="description">Description</label>
                    <textarea class="form-control" id="description" name="description" placeholder="Description" required></textarea>
                </div>
                <div class="col-md-12 form-group">
                    <label for="stock">Stock</label>
                    <input type="number" class="form-control" id="stock" name="stock" placeholder="Stock" required>
                </div>
                <div class="col-md-12 form-group">
                    <label for="brandName">Brand Name</label>
                    <input type="text" class="form-control" id="brandName" name="brandName" placeholder="Brand Name" required>
                </div>


                <!-- Main Image Upload -->
                <div class="col-md-12 form-group">
                    <label for="mainImage">Main Image</label>
                    <input type="file" class="form-control" id="mainImage" name="mainImage" accept="image/*" required>
                </div>
                <!-- Additional Images Display and Upload -->
                <div class="col-md-12 form-group">
                    <label for="additionalImages">Add Additional Images</label>
                    <input type="file" class="form-control" id="additionalImages" name="additionalImages" accept="image/*" multiple>
                </div>

                <!-- Product Specs Fields -->
                <div class="col-md-12 form-group">
                    <label for="processor">Processor</label>
                    <input type="text" class="form-control" id="processor" name="processor" placeholder="Processor" required>
                </div>
                <div class="col-md-12 form-group">
                    <label for="memory">Memory (GB)</label>
                    <input type="number" class="form-control" id="memory" name="memory" placeholder="Memory (GB)" required>
                </div>
                <div class="col-md-12 form-group">
                    <label for="storage">Storage</label>
                    <input type="text" class="form-control" id="storage" name="storage" placeholder="Storage" required>
                </div>
                <div class="col-md-12 form-group">
                    <label for="graphicsCard">Graphics Card</label>
                    <input type="text" class="form-control" id="graphicsCard" name="graphicsCard" placeholder="Graphics Card" required>
                </div>
                <div class="col-md-12 form-group">
                    <label for="displaySize">Display Size</label>
                    <input type="text" class="form-control" id="displaySize" name="displaySize" placeholder="Display Size" required>
                </div>
                <div class="col-md-12 form-group">
                    <label for="batteryLife">Battery Life (Hours)</label>
                    <input type="number" class="form-control" id="batteryLife" name="batteryLife" placeholder="Battery Life (Hours)" required>
                </div>
                <div class="col-md-12 form-group">
                    <label for="os">Operating System</label>
                    <input type="text" class="form-control" id="os" name="os" placeholder="Operating System" required>
                </div>
                <div class="col-md-12 form-group">
                    <label for="weight">Weight (kg)</label>
                    <input type="number" class="form-control" id="weight" name="weight" placeholder="Weight (kg)" step="0.01" required>
                </div>

                <!-- Category Selection -->
                <div class="col-md-12 form-group">
                    <label for="category">Select Category</label>
                    <select class="form-control" id="category" name="category" required>
                        <option value="" disabled selected>Select Category</option>
                        <c:if test="${categoryList != null}">
                            <c:forEach items="${categoryList}" var="category">
                                <option value="${category.getId()}">${category.getName()}</option>
                            </c:forEach>
                        </c:if>
                        <!-- Add more categories dynamically if needed -->
                    </select>
                </div>
                <div class="col-md-12 form-group">
                    <button type="button" class="primary-btn" id="uploadImagesBtn">Add Product</button>
                </div>
            </form>
            <div id="loader" class="loader-container">
                <div class="loader"></div>
            </div>
        </div>
    </div>
</section>

<script type="module">
    import {firebaseConfig} from "./firebase.js";
    import { initializeApp } from "https://www.gstatic.com/firebasejs/10.13.1/firebase-app.js";
    import { getStorage, ref, uploadBytes, getDownloadURL } from "https://www.gstatic.com/firebasejs/10.13.1/firebase-storage.js";

    const app = initializeApp(firebaseConfig);
    const storage = getStorage(app);
    const mainImageFile = document.getElementById('mainImage');
    const additionalImagesFile = document.getElementById('additionalImages');
    const updateProductForm = document.getElementById('updateProductForm');
    let imageUrls = [];

    document.getElementById('uploadImagesBtn').addEventListener('click', async function () {
        if (!validateForm()) {
            return;
        }
        showLoader();

        // Reset imageUrls to capture fresh uploads only
        imageUrls = [];

        if (mainImageFile.files[0]) {
            const file = mainImageFile.files[0];
            const storageRef = ref(storage, file.name);
            await uploadBytes(storageRef, file);
            const mainImageUrl = await getDownloadURL(storageRef);
            imageUrls.push(mainImageUrl); // Add main image URL
        } else {
            imageUrls.push("${product.image}"); // Keep existing main image if no new one is uploaded
        }

        // Handle additional images upload
        if (additionalImagesFile.files.length > 0) {
            for (const file of additionalImagesFile.files) {
                const storageRef = ref(storage, file.name);
                await uploadBytes(storageRef, file);
                const additionalImageUrl = await getDownloadURL(storageRef);
                imageUrls.push(additionalImageUrl); // Add additional image URLs
            }
        }


        // After all images are uploaded, submit the form
        sendForm();
    });


    // Upload additional images


    // Handle form submission
    function sendForm() {

    if (imageUrls.length) {
    // Form data to be sent to backend
        const product = {
            name: document.getElementById('name').value,
            price: document.getElementById('price').value * 100,
            category: { id: document.getElementById('category').value },
            image: imageUrls.length ? imageUrls[0] : ``,
            description: document.getElementById('description').value,
            stock: document.getElementById('stock').value,
            brandName: document.getElementById('brandName').value,
            imageURLs: imageUrls.slice(0, 3),
            specs: {
                processor: document.getElementById('processor').value,
                memory: document.getElementById('memory').value,
                storage: document.getElementById('storage').value,
                graphicsCard: document.getElementById('graphicsCard').value,
                displaySize: document.getElementById('displaySize').value,
                batteryLife: document.getElementById('batteryLife').value,
                os: document.getElementById('os').value,
                weight: document.getElementById('weight').value
            },
            images: imageUrls.slice(1, 3).map(url => ({ url: url })) // First two as objects if available
        };

        // Send form data to your backend
        fetch(`/products`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(product),
        }).then(response => {
            if (response.status === 200) {
                hideLoader();
                console.log("Product added successfully");
                location.reload();
                alert("Product added successfully");
            } else {
                hideLoader();
                alert("Failed adding the product");
            }
        });
        } else {
            alert("failed saving the order");
            uploadStatus.innerHTML = '<p>Please upload all images before submitting the form.</p>';
        }
    }

</script>
<script>
    // Show the loader
    function showLoader() {
        document.getElementById('loader').style.visibility = 'visible';
    }

    // Hide the loader
    function hideLoader() {
        document.getElementById('loader').style.visibility = 'hidden';
    }

    function validateForm() {
        // Get input values
        const price = document.getElementById('price').value;
        const weight = document.getElementById('weight').value;
        const memory = document.getElementById('memory').value;
        const batteryLife = document.getElementById('batteryLife').value;
        const stock = document.getElementById('stock').value;

        // Validate Price (max 2 decimal places)
        if (!/^\d+(\.\d{1,2})?$/.test(price)) {
            alert("Price must be a number with up to 2 decimal places.");
            return false;
        }

        // Validate Weight (min 1.2, max 2 decimal places)
        if (!/^\d+(\.\d{1,2})?$/.test(weight) || parseFloat(weight) < 1.2) {
            alert("Weight must be at least 1.2 and can have up to 2 decimal places.");
            return false;
        }

        // Validate Memory (must be an integer)
        if (!/^\d+$/.test(memory)) {
            alert("Memory must be an integer.");
            return false;
        }

        // Validate Battery Life (must be an integer)
        if (!/^\d+$/.test(batteryLife)) {
            alert("Battery life must be an integer.");
            return false;
        }

        if (!/^\d+$/.test(stock)) {
            alert("Stock must be an integer.");
            return false;
        }
        // If all validations pass, return true to allow form submission
        return true;
    }

</script>
<script src="../assets/js/vendor/jquery-2.2.4.min.js"></script>
<script src="../assets/js/vendor/bootstrap.min.js"></script>
<script src="../assets/js/main.js"></script>

</body>

</html>
