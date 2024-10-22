package com.example.electro.repository;

import com.example.electro.model.Product;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductDAO extends JpaRepository<Product,Integer>, Specification<Product> {

    List<Product> findProductsByCategory(String name);


    //-----------Specification---------------
    public List<Product> findByFilter(Map<String, Object> filter, int page,
                                      int size) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> product = cq.from(Product.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(product.<Boolean>get("deleted"),false));

        if(filter.containsKey("category")){
            predicates.add(cb.equal(product.<Category>get("category").<Integer>get(
                            "id"),
                    (Integer)filter.get("category")));
        }

        if(filter.containsKey("brand")){
            predicates.add(cb.equal(product.<String>get("brandName"),
                    filter.get("brand")));
        }

        if(filter.containsKey("processor")){
            predicates.add(cb.equal(product.<ProductSpecs>get("specs").get(
                    "processor"),
                    (String)filter.get("processor")));
        }
        if(filter.containsKey("memory")){
            predicates.add(cb.equal(product.<ProductSpecs>get("specs").<Integer>get("memory"), (Integer)filter.get("memory")));
        }
        if(filter.containsKey("storage")){
            predicates.add(cb.equal(product.<ProductSpecs>get("specs").get("storage"), (String)filter.get("storage")));
        }
        if(filter.containsKey("graphicsCard")){
            predicates.add(cb.equal(product.<ProductSpecs>get("specs").get("graphicsCard"), (String)filter.get("graphicsCard")));
        }
        if(filter.containsKey("displaySize")){
            predicates.add(cb.equal(product.<ProductSpecs>get("specs").get("displaySize"), (String)filter.get("displaySize")));
        }
        if(filter.containsKey("batteryLife")){
            predicates.add(cb.equal(product.<ProductSpecs>get("specs").<Integer>get("batteryLife"), (Integer)filter.get("batteryLife")));
        }
        if(filter.containsKey("os")){
            predicates.add(cb.equal(product.<ProductSpecs>get("specs").get("os"), (String)filter.get("os")));
        }

        if(filter.containsKey("min-price")){
            predicates.add(cb.greaterThanOrEqualTo(product.<Integer>get("price"),
                    (Integer)filter.get("min-price")));
        }
        if(filter.containsKey("max-price")){
            predicates.add(cb.lessThanOrEqualTo(product.<Integer>get("price"),
                    (Integer)filter.get("max-price")));
        }
        if(filter.containsKey("name")){
            predicates.add(cb.like(product.<String>get("name"),
                    "%"+(String)filter.get(
                            "name")+"%"));
        }
        cq.where(predicates.toArray(new Predicate[0]));
        Query query =  em.createQuery(cq);
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
        return query.getResultList();
    }


    // edit delete to make soft delete and setDeleted -> true
    boolean deleteById(int id);

    Product addWithImages(Product product, List<String> imageUrls) {
        try {
            em.getTransaction().begin();
            em.persist(product);
            for(int i = 1; i < imageUrls.size(); i++){
                String imageUrl = imageUrls.get(i);
                Image image = new Image();
                image.setUrl(imageUrl);
                image.setProduct(product);
                em.persist(image);
            }
            em.getTransaction().commit();
            return product;
        }
        catch (Exception e){
            em.getTransaction().rollback();
            return null;
        }

    }

    public Product updateWithImages(Product product, List<String> imageUrls) {
        try {
            em.getTransaction().begin();

            if(!imageUrls.isEmpty()){
                product.setImage(imageUrls.get(0));
            }

            System.out.println(product.getImages());
            System.out.println(product);
            if(!imageUrls.isEmpty()){
                for(Image image : product.getImages()){
                    em.remove(image);
                }
                for(int i = 1; i < imageUrls.size(); i++){
                    String imageUrl = imageUrls.get(i);
                    Image image = new Image();
                    image.setUrl(imageUrl);
                    image.setProduct(product);
                    em.persist(image);
                }
            }
            product.setSpecs(em.merge(product.getSpecs()));
            product = em.merge(product);
            em.getTransaction().commit();
            return product;
        }
        catch (Exception e){
            em.getTransaction().rollback();
            return null;
        }

    }

}
