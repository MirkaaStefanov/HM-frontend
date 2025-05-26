document.addEventListener('DOMContentLoaded', function() {

    // Like button functionality (visual toggle only)
    const likeButtons = document.querySelectorAll('.like-button');
    likeButtons.forEach(button => {
        button.addEventListener('click', function(event) {
            event.stopPropagation(); // Prevents card click if any
            // Toggle heart icon (solid vs. regular)
            const heartIcon = this.querySelector('i');
            if (heartIcon.classList.contains('far')) { // If it's a regular heart
                heartIcon.classList.remove('far');
                heartIcon.classList.add('fas'); // Change to solid heart
                this.classList.add('liked');
            } else { // If it's a solid heart
                heartIcon.classList.remove('fas');
                heartIcon.classList.add('far'); // Change back to regular
                this.classList.remove('liked');
            }

            // Here you would typically make an API call to update the like status on the server
            // const productId = this.closest('.product-card').dataset.productId; // If you add data-product-id
            // console.log('Liked/unliked product:', productId);
        });
    });

   document.addEventListener('DOMContentLoaded', () => {
       const searchInput = document.getElementById('global-search-input');
       const productGrid = document.getElementById('product-grid-container');
       const noProductsMessage = document.getElementById('no-products-message'); // Get the no products message div

       // allProducts is passed from Thymeleaf via th:inline="javascript"
       // It will be an array of ProductDTO objects
       // Example structure:
       // [
       //   { name: "Apartment A", description: "City view", price: 300000, area: 100, state: { name: "FOR_SALE", displayText: "За продажба" } },
       //   { name: "House B", description: "Garden", price: 600000, area: 200, state: { name: "RENTED", displayText: "Дадено под наем" } }
       // ]

       if (!allProducts || allProducts.length === 0) {
           if (noProductsMessage) {
               noProductsMessage.style.display = 'block'; // Show the message if no products
           }
           if (productGrid) {
                productGrid.innerHTML = ''; // Clear grid if no products
           }
           return; // Exit if no products to filter
       } else {
           if (noProductsMessage) {
               noProductsMessage.style.display = 'none'; // Hide initially if products are present
           }
       }


       const renderProducts = (productsToRender) => {
           if (!productGrid) return; // Exit if product grid element is not found

           productGrid.innerHTML = ''; // Clear current products

           if (productsToRender.length === 0) {
               if (noProductsMessage) {
                   noProductsMessage.style.display = 'block'; // Show if no matching products
               }
               return;
           } else {
               if (noProductsMessage) {
                   noProductsMessage.style.display = 'none'; // Hide if products are found
               }
           }


           productsToRender.forEach(product => {
               const productCard = document.createElement('div');
               productCard.classList.add('product-card');

               const imageUrl = product.imageStrings && product.imageStrings.length > 0
                                ? `data:image/jpeg;base64,${product.imageStrings[0]}`
                                : '/img/photos/noImage.png'; // Use default if no image

               productCard.innerHTML = `
                   <div class="product-image-container">
                       <img src="${imageUrl}" alt="Property Image" class="product-image">
                       <button class="like-button"><i class="far fa-heart"></i></button>
                   </div>
                   <div class="product-info">
                       <h3 class="product-name">${product.name}</h3>
                       <p class="product-price">$${product.price.toLocaleString('en-US')}</p>
                       <div class="product-details">
                           <span>3 beds</span> <span>&middot;</span>
                           <span>2 baths</span> <span>&middot;</span>
                           <span>${product.area} sq ft</span>
                           ${product.state ? `<span> &middot; </span><span class="product-state">${product.state.displayText}</span>` : ''}
                       </div>
                   </div>
               `;
               productGrid.appendChild(productCard);
           });
       };

       const applySearchFilter = () => {
           const searchTerm = searchInput.value.toLowerCase().trim();

           const filteredProducts = allProducts.filter(product => {
               const nameMatch = product.name ? product.name.toLowerCase().includes(searchTerm) : false;
               const descriptionMatch = product.description ? product.description.toLowerCase().includes(searchTerm) : false;
               const stateMatch = product.state && product.state.displayText ? product.state.displayText.toLowerCase().includes(searchTerm) : false;
               const priceMatch = product.price ? product.price.toString().includes(searchTerm) : false;
               const areaMatch = product.area ? product.area.toString().includes(searchTerm) : false;

               return nameMatch || descriptionMatch || stateMatch || priceMatch || areaMatch;
           });

           renderProducts(filteredProducts);
       };

       // Initial render of all products
       renderProducts(allProducts);

       // Add event listener for real-time search
       searchInput.addEventListener('keyup', applySearchFilter);
       searchInput.addEventListener('change', applySearchFilter); // For clear button
   });

});