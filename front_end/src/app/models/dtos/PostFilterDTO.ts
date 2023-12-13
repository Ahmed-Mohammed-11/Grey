import Feeling from "@/app/models/dtos/Feeling";

class PostFilterDTO {
    pageSize!: number;
    pageNumber!: number;
    day?: number;
    month?: number;
    year?: number;
    feelings?:Feeling[]
}

export default PostFilterDTO;