<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update Product</title>
    <link rel="stylesheet" href="/../assets/css/bootstrap.css">
    <link rel="stylesheet" href="/../assets/css/main.css">
    <link rel="stylesheet" href="/../assets/css/all.css">
    <link rel="stylesheet" href="/../assets/css/style.css">

    <style>
        /* Hide the default file input by making it transparent */
        .custom-file-input {
            opacity: 0;
            position: absolute;
            width: 100%;
            height: 100%;
            cursor: pointer;
        }

        /* Style the custom label as a button */
        .custom-file-label-wrapper {
            display: inline-block;
            position: relative;
            background-color: #4CAF50; /* Green background */
            color: white;
            border-radius: 4px;
            padding: 10px 20px;
            cursor: pointer;
            transition: background-color 0.3s;
            margin-top: 10px;
            overflow: hidden;
        }

        /* Adjust hover and active styles */
        .custom-file-label-wrapper:hover {
            background-color: #45a049;
        }

        /* Consistent image styling */
        .product-image, .additional-images img {
            max-width: 100px;
            border: 1px solid #ddd;
            border-radius: 4px;
            margin: 5px 10px 0 0;
        }

        /* Loader styling */
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

        /* Notification Container */
        .notification-container {
            position: fixed;
            top: 20px;
            right: 20px;
            z-index: 1000;
            display: flex;
            flex-direction: column;
            gap: 10px;
        }

        /* Notification Styling */
        .notification {
            background-color: #4caf50;
            color: white;
            padding: 15px 20px;
            border-radius: 5px;
            box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
            opacity: 1;
            cursor: pointer;
            transition: opacity 0.5s ease;
            margin-bottom: 10px;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }

        /* Different colors for notification types */
        .notification.success { background-color: #4caf50; }
        .notification.error { background-color: #f44336; }
        .notification.warning { background-color: #ff9800; }

        /* Dismiss button */
        .notification .dismiss {
            margin-left: 10px;
            cursor: pointer;
            font-weight: bold;
        }

        /* Fade In and Out Animations */
        @keyframes fade-in {
            from { opacity: 0; transform: translateY(-10px); }
            to { opacity: 1; transform: translateY(0); }
        }

        @keyframes fade-out {
            from { opacity: 1; }
            to { opacity: 0; }
        }

    </style>




</head>

<body>

<c:import url="admin-header.jsp" />

<section class="container section_gap">
    <div class="row">
        <div class="col-lg-6 mx-auto">
            <form class="row login_form" id="updateProductForm">
                <input type="hidden" name="id" value="${product.id}">

                <div class="col-md-12 form-group">
                    <label for="name">Product Name</label>
                    <input type="text" class="form-control" id="name" name="name" value="${product.name}" required>
                </div>
                <div class="col-md-12 form-group">
                    <label for="price">Price</label>
                    <input type="number" class="form-control" id="price" name="price" value="${product.price / 100}" required step="0.01">
                </div>
                <div class="col-md-12 form-group">
                    <label for="description">Description</label>
                    <textarea class="form-control" id="description" name="description" required>${product.description}</textarea>
                </div>
                <div class="col-md-12 form-group">
                    <label for="stock">Stock</label>
                    <input type="number" class="form-control" id="stock" name="stock" value="${product.stock}" required>
                </div>
                <div class="col-md-12 form-group">
                    <label for="brandName">Brand Name</label>
                    <select class="form-control" id="brandName" name="brandName" required>
                        <option value="" disabled>Select Brand</option>
                        <c:if test="${brandList != null}">
                            <c:forEach items="${brandList}" var="brandName">
                                <option value="${brandName}" ${brandName == product.brandName ? 'selected' : ''}>${brandName}</option>
                            </c:forEach>
                        </c:if>
                    </select>
                </div>
                <div class="col-md-12 form-group clearfix">
                    <label for="mainImage">Main Image</label>
                    <div class="upload-group">
                        <!-- Wrapper for the file input and custom label -->
                        <div class="custom-file-label-wrapper">
                            <input type="file" class="custom-file-input" id="mainImage" name="mainImage" accept="image/*">
                            <span>Choose Image</span>
                        </div>
                        <img src="${product.image}" alt="Product Image" class="product-image">
                    </div>
                </div>

                <div class="col-md-12 form-group clearfix">
                    <label for="additionalImages">Additional Images</label>
                    <div class="upload-group">
                        <div class="custom-file-label-wrapper">
                            <input type="file" class="custom-file-input" id="additionalImages" name="additionalImages" accept="image/*" multiple>
                            <span>Choose Images</span>
                        </div>
                        <div class="additional-images">
                            <c:if test="${product.images != null && !product.images.isEmpty()}">
                                <p>Additional Images:</p>
                                <c:forEach items="${product.images}" var="image">
                                    <img src="${image.url}" alt="Product Image">
                                </c:forEach>
                            </c:if>
                        </div>
                    </div>
                </div>


                <div class="col-md-12 form-group">
                    <label for="processor">Processor</label>
                    <select class="form-control" id="processor" name="processor" required>
                        <option value="" disabled>Select Processor</option>
                        <c:if test="${processorList != null}">
                            <c:forEach items="${processorList}" var="processor">
                                <option value="${processor}" ${processor == product.specs.processor ? 'selected' : ''}>${processor}</option>
                            </c:forEach>
                        </c:if>
                    </select>
                </div>

                <div class="col-md-12 form-group">
                    <label for="memory">Memory (GB)</label>
                    <select class="form-control" id="memory" name="memory" required>
                        <option value="" disabled>Select Memory</option>
                        <c:if test="${memoryList != null}">
                            <c:forEach items="${memoryList}" var="memory">
                                <option value="${memory}" ${memory == product.specs.memory ? 'selected' : ''}>${memory}</option>
                            </c:forEach>
                        </c:if>
                    </select>
                </div>
                <div class="col-md-12 form-group">
                    <label for="storage">Storage</label>
                    <input type="text" class="form-control" id="storage" name="storage" value="${product.specs.storage}" required>
                </div>
                <div class="col-md-12 form-group">
                    <label for="graphicsCard">Graphics Card</label>
                    <input type="text" class="form-control" id="graphicsCard" name="graphicsCard" value="${product.specs.graphicsCard}" required>
                </div>
                <div class="col-md-12 form-group">
                    <label for="displaySize">Display Size</label>
                    <input type="text" class="form-control" id="displaySize" name="displaySize" value="${product.specs.displaySize}" required>
                </div>
                <div class="col-md-12 form-group">
                    <label for="batteryLife">Battery Life (Hours)</label>
                    <input type="number" class="form-control" id="batteryLife" name="batteryLife" value="${product.specs.batteryLife}" required>
                </div>
                <div class="col-md-12 form-group">
                    <label for="os">Operating System</label>
                    <select class="form-control" id="os" name="batteryLife" required>
                        <option value="" disabled>Select Operating System</option>
                        <c:if test="${osList != null}">
                        <c:forEach items="${osList}" var="os">
                        <option value="${os}" ${os == product.specs.os ? 'selected' : ''}>${os}</option>
                        </c:forEach>
                        </c:if>
                    </select>
                </div>
                <div class="col-md-12 form-group">
                    <label for="weight">Weight (kg)</label>
                    <input type="number" class="form-control" id="weight" name="weight" value="${product.specs.weight}" step="0.01" required>
                </div>

                <div class="col-md-12 form-group">
                    <label for="category">Select Category</label>
                    <select class="form-control" id="category" name="category" required>
                        <option value="" disabled>Select Category</option>
                        <c:if test="${categoryList != null}">
                            <c:forEach items="${categoryList}" var="category">
                                <option value="${category.getId()}" ${category.getId() == product.category.id ? 'selected' : ''}>${category.getName()}</option>
                            </c:forEach>
                        </c:if>
                    </select>
                </div>

                <div class="col-md-12 form-group">
                    <button type="button" class="primary-btn" id="uploadImagesBtn">Update Product</button>
                </div>
            </form>
            <div id="loader" class="loader-container">
                <div class="loader"></div>
            </div>
            <div id="notification-container" class="notification-container"></div>

        </div>
    </div>
</section>

<script type="module">

    import {firebaseConfig} from "../firebase.js";
    import { initializeApp } from "https://www.gstatic.com/firebasejs/10.13.1/firebase-app.js";
    import { getStorage, ref, uploadBytes, getDownloadURL } from "https://www.gstatic.com/firebasejs/10.13.1/firebase-storage.js";

    const app = initializeApp(firebaseConfig);
    const storage = getStorage(app);
    const mainImageFile = document.getElementById('mainImage');
    const additionalImagesFile = document.getElementById('additionalImages');
    const updateProductForm = document.getElementById('updateProductForm');
    let imageUrls = [];

    document.getElementById('uploadImagesBtn').addEventListener('click', async function (event) {
        event.preventDefault();
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

        sendForm();
    });


    function sendForm() {

        const product = {
            id: ${id},
            name: document.getElementById('name').value,
            price: document.getElementById('price').value * 100,
            category: { id: document.getElementById('category').value },
            image: imageUrls.length ? imageUrls[0] : `${product.image}`,
            description: document.getElementById('description').value,
            stock: document.getElementById('stock').value,
            brandName: document.getElementById('brandName').value,
            imageURLs: imageUrls.slice(0, 3),
            specs: {
                id:${product.specs.id},
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

        fetch(`/products/${product.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json' // Ensure this is set to application/json
            },
            body: JSON.stringify(product), // Send as FormData
        }).then(response => {
            if (response.status === 200) {
                hideLoader();
                console.log("Product updated successfully");
                // location.reload();
                showNotification("Product updated successfully!", "success", 3000);
                setTimeout(() => {
                    location.reload();
                }, 3000);
            } else {
                hideLoader();
                showNotification("Failed updating the product", "error", 3000);
            }
        });
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
            // alert("Price must be a number with up to 2 decimal places.");
            showNotification("Price must be a number with up to 2 decimal places.", "warning", 3000);

            return false;
        }

        // Validate Weight (min 1.2, max 2 decimal places)
        if (!/^\d+(\.\d{1,2})?$/.test(weight) || parseFloat(weight) < 1.2) {
            // alert("Weight must be at least 1.2 and can have up to 2 decimal places.");
            showNotification("Weight must be at least 1.2 and can have up to 2 decimal places.", "warning", 3000);
            return false;
        }

        // Validate Memory (must be an integer)
        if (!/^\d+$/.test(memory)) {
            // alert("Memory must be an integer.");
            showNotification("Memory must be an integer.", "warning", 3000);
            return false;
        }

        // Validate Battery Life (must be an integer)
        if (!/^\d+$/.test(batteryLife)) {
            // alert("Battery life must be an integer.");
            showNotification("Battery life must be an integer.", "warning", 3000);
            return false;
        }

        if (!/^\d+$/.test(stock)) {
            // alert("Stock must be an integer.");
            showNotification("Stock must be an integer.", "warning", 3000);
            return false;
        }
        // If all validations pass, return true to allow form submission
        return true;
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


</script>
<script src="/../assets/js/notifications.js"></script>
<script src="/../assets/js/vendor/jquery-2.2.4.min.js"></script>
<script src="/../assets/js/vendor/bootstrap.min.js"></script>
<script src="/../assets/js/main.js"></script>

</body>

</html>
