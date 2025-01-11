import {
  Body,
  Controller,
  Get,
  Param,
  Post,
  Put,
  Res,
  UploadedFile,
  UseInterceptors,
} from '@nestjs/common';
import { FileInterceptor } from '@nestjs/platform-express';
import { AppService } from './app.service';
import { ApiBody, ApiConsumes } from '@nestjs/swagger';
import { Response } from 'express';

@Controller()
@UseInterceptors(FileInterceptor('file'))
export class AppController {
  constructor(private readonly appService: AppService) {}

  @Post('/upload')
  @ApiConsumes('multipart/form-data')
  @ApiBody({
    schema: {
      type: 'object',
      properties: {
        file: {
          type: 'string',
          format: 'binary',
        },
      },
    },
  })
  uploadFile(@UploadedFile('file') file: Express.Multer.File) {
    return this.appService.saveFile(file);
  }

  @Get('/file/:id')
  async getFile(@Param('id') id: string, @Res() response: Response) {
    const readStream = await this.appService.getFileById(id);
    readStream.pipe(response);
  }

  @Put('/file/:id')
  @ApiConsumes('multipart/form-data')
  @ApiBody({
    schema: {
      type: 'object',
      properties: {
        file: {
          type: 'string',
          format: 'binary',
        },
      },
    },
  })
  replaceFile(
    @Param('id') id: string,
    @UploadedFile('file') file: Express.Multer.File,
  ) {
    return this.appService.replaceFile(id, file);
  }
}
