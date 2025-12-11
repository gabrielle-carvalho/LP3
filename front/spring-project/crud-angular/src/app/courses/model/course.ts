import { Lesson } from './lesson';

export interface Course {
  _id: string; // O Backend manda como "_id" por causa do @JsonProperty
  name: string;
  category: string;
  lessons?: Lesson[];
}
