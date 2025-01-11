import {
  BadRequestException,
  Injectable,
  NotFoundException,
  OnModuleInit,
} from '@nestjs/common';
import { randomUUID } from 'crypto';
import { createReadStream } from 'fs';
import { mkdir, readFile, writeFile, rm } from 'fs/promises';
import { glob } from 'glob';

enum MediaType {
  IMAGE = 'image',
  VIDEO = 'video',
}

@Injectable()
export class AppService implements OnModuleInit {
  async onModuleInit() {
    await mkdir('./files', { recursive: true });
  }

  async getFileById(id: string) {
    const files = await glob(`./files/${id}-*`);
    if (!files.length) throw new NotFoundException('File not found');
    return createReadStream(`./${files[0]}`);
  }

  async saveFile(file: Express.Multer.File) {
    const fileUuid = randomUUID();
    const [fileType] = file.mimetype.split('/');

    await writeFile(`./files/${fileUuid}-${file.originalname}`, file.buffer);

    return { id: fileUuid, type: fileType };
  }

  async replaceFile(fileUuid: string, file: Express.Multer.File) {
    const files = await glob(`./files/${fileUuid}-*`);
    if (files[0]) {
      await rm(`./${files[0]}`, { recursive: true });
    }

    await writeFile(`./files/${fileUuid}-${file.originalname}`, file.buffer);
    return { id: fileUuid, type: file.mimetype.split('/')[0] };
  }
}
