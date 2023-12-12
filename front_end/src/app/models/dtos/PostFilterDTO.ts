import Feeling from "@/app/models/dtos/Feeling";

class PostFilterDTO {
    pageSize!: number;
    pageNumber!: number;
    day?: number;
    month?: number;
    year?: number;
    feelings?:Set<Feeling>
}

export default PostFilterDTO;