<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Product List</title>
    <link rel="stylesheet" href="../assets/css/bootstrap.css">
    <link rel="stylesheet" href="../assets/css/main.css">
    <link rel="stylesheet" href="../assets/css/all.css">
    <link rel="stylesheet" href="../assets/css/style.css">
    <style>
        .btn-secondary {
            background-color: #6c757d; /* Bootstrap's secondary color */
            cursor: not-allowed;
            opacity: 0.65;
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
<br>
<br>
<br>
<br>
<br>
<div class="search_input" id="search_input_box" style="z-index: 1">
    <div class="container">
        <form class="d-flex justify-content-between" action="products" method="get">
            <input
                    type="text"
                    class="form-control"
                    id="search_input"
                    placeholder="Search Here"
                    name="search_input"
                    value="${param.search_input}"
                    onblur="this.form.submit()"
            />
<%--            <button type="submit" class="btn btn-primary">Search</button>--%>
            <span
                    class="lnr lnr-cross"
                    id="close_search"
                    title="Close Search"
                    style="cursor: pointer;"
                    onclick="document.getElementById('search_input').value='';"
            ></span>
        </form>
    </div>
</div>

<section class="container section_gap">
    <span>
        <h3 style="display: inline-block">Product List</h3>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <button class="btn btn-success" onclick="window.location.href='/dashboard/create-product'">Add Product</button>
    </span>
    <!-- Product Table -->
    <table class="table table-striped">
        <thead>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Price</th>
            <th>Stock</th>
            <th>Category</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody id="productTableBody">
        <c:forEach items="${products}" var="product">
            <tr>
                <td>${product.id}</td>
                <td>${product.name}</td>
                <td>
                    <fmt:formatNumber value="${product.price / 100}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
                </td>
                <td>${product.stock}</td>
                <td>${product.brandName}</td>
                <td>
                    <c:choose>
                        <c:when test="${product.deleted}">
                            <button class="btn btn-secondary disabled" >Deleted</button>
                        </c:when>
                        <c:otherwise>
                            <button id="delete-btn-${product.id}" class="btn btn-danger" onclick="confirmDelete(${product.id})">Delete</button>
                        </c:otherwise>
                    </c:choose>
                    <button class="btn btn-primary" onclick="window.location.href='/dashboard/products/${product.id}'">Update</button>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <div id="notification-container" class="notification-container"></div>

    <!-- Pagination -->
    <nav aria-label="Page navigation">
        <ul class="pagination">
            <li class="page-item ${page == 1 ? 'disabled' : ''}">
                <button class="page-link" onclick="redirect(${page - 1})" aria-label="Previous">
                    <span aria-hidden="true">&laquo;</span>
                </button>
            </li>
            <c:forEach begin="1" end="${totalPages}" var="i">
                <li class="page-item ${page == i ? 'active' : ''}">
                    <button class="page-link" onclick="redirect(${i})">${i}</button>
                </li>
            </c:forEach>
            <li class="page-item ${page == totalPages ? 'disabled' : ''}">
                <button class="page-link" onclick="redirect(${page + 1})" aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                </button>
            </li>
        </ul>
    </nav>
</section>

<script>
    // Confirm delete action
    function confirmDelete(productId) {
        const isConfirmed = confirm("Are you sure you want to delete this product?");
        if (isConfirmed) {
            deleteProduct(productId);
        }
    }

    // Function to delete the product
    function deleteProduct(productId) {
        fetch(`/products/`+productId, {
            method: 'DELETE',
        }).then(response => {
            if (response.ok) {
                // alert('Product deleted successfully');
                // window.location.reload();
                showNotification("Product deleted successfully!", "success", 3000);
                setTimeout(() => {
                    window.location.reload();
                }, 3000);
            } else {
                // alert('Failed to delete the product');
                showNotification("Failed to delete the product", "error", 3000);
            }
        }).catch(error => {
            console.error('Error:', error);
            // alert('An error occurred while trying to delete the product');
            showNotification("An error occurred while trying to delete the product", "error", 3000);
        });
    }

    function redirect(pageNum) {
        const urlParams = new URLSearchParams(window.location.search);
        let searchInput =  urlParams.get("search_input");
        if (searchInput == null){
            window.location.href=`${window.location.origin}${window.location.pathname}?page=`+pageNum
        }
        else {
            window.location.href=`${window.location.origin}${window.location.pathname}?page=`+pageNum+"&search_input="+searchInput;
        }
    }

</script>
<script src="/../assets/js/notifications.js"></script>
<script src="../assets/js/vendor/jquery-2.2.4.min.js"></script>
<script src="../assets/js/vendor/bootstrap.min.js"></script>
<script src="../assets/js/main.js"></script>

</body>

</html>
